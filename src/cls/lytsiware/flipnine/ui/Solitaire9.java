package cls.lytsiware.flipnine.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import cls.lytsiware.common.animation.FxThreadAnimate;
import cls.lytsiware.flipnine.ai.FlipSolver;
import cls.lytsiware.flipnine.ai.FlipSolver.Mode;
import cls.lytsiware.flipnine.ai.FlipSolverService;
import cls.lytsiware.flipnine.ui.external.ExternalAdapter;
import cls.lytsiware.flipnine.ui.external.ExternalInterface;
import cls.lytsiware.flipnine.ui.undo.UndoManager;

public class Solitaire9 extends Application {

	private static final int APP_WIDTH = 800;
	private static final int APP_HEIGHT = 400;

	List<CardUi> cards;
	/**
	 * Mouse handling and application business logic
	 */
	protected ApplicationMouseHandler mouseHandler;

	/**
	 * allows interaction with external applications
	 */
	protected ExternalInterface externalAdapter;

	/**
	 * Finds the solution
	 */
	protected FlipSolver flipSolver;

	/**
	 * Responsible for handling the animations
	 */
	protected FxThreadAnimate fxAnimate;

	/**
	 * Responsible for undo operations
	 */
	protected UndoManager undoManager;

	/**
	 * the root node to add to the scene
	 */
	protected Group root;

	/**
	 * Service to execute the solving process in a separate Thread
	 */
	FlipSolverService flipSolverService;

	/**
	 * Buttons
	 */
	Button solveButton;
	Button newGameButton;
	Button undoButon;
	ToggleButton extendedVersion;

	/**
	 * The one and only..!
	 */
	protected Stage primaryStage;

	@Override
	public void start(final Stage primaryStage) {
		this.primaryStage = primaryStage;
		initialise();
	}

	private void initialise() {
		root = new Group();
		cards = new ArrayList<>();
		initCards();
		solveButton = new Button("solve");
		solveButton.setDisable(true);
		newGameButton = new Button("New");
		undoButon = new Button("Undo");
		extendedVersion = new ToggleButton("Extended");
		extendedVersion.setSelected(true);

		undoButon.setDisable(true);

		flipSolver =  new FlipSolver(Mode.SIMPLE);
		fxAnimate = new FxThreadAnimate();
		undoManager = new UndoManager(fxAnimate, cards);
		flipSolverService = new FlipSolverService(flipSolver);
		mouseHandler = new ApplicationMouseHandler(this);
		externalAdapter = new ExternalAdapter(mouseHandler, fxAnimate, cards);

		root.setOnMouseClicked(mouseHandler);

		newGameButton.getStyleClass().add("buttonClass");
		newGameButton.setId("new-game-button");
		solveButton.getStyleClass().add("buttonClass");
		solveButton.setId("solve-button");
		solveButton.relocate(100, 0);
		undoButon.getStyleClass().add("buttonClass");
		undoButon.setId("undo-button");
		undoButon.relocate(300, 0);
		extendedVersion.getStyleClass().add("buttonClass");
		extendedVersion.relocate(420, 0);
		root.getChildren().add(newGameButton);
		root.getChildren().add(solveButton);
		root.getChildren().add(undoButon);
		root.getChildren().add(extendedVersion);
		root.getChildren().add(0, createBackground());
		Scene mainScene = new Scene(root, APP_WIDTH, APP_HEIGHT);
		mainScene.getStylesheets().add("css/stylesheet.css");
		primaryStage.setScene(mainScene);
		primaryStage.show();


		flipSolverService.addEventHandler(WorkerStateEvent.ANY, new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent event) {

				if (!event.getEventType().toString().equals(WorkerStateEvent.WORKER_STATE_RUNNING.toString())) {
					solveButton.setDisable(extendedVersion.isSelected() || false);
				}else {
					solveButton.setDisable(true);
				}
			}
		});

		flipSolverService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				solveButton.setDisable(false);
				List<Integer> solution = flipSolverService.getValue();
				if (!solution.isEmpty()) {
					CardUi cardToActivate = null;

					for (CardUi card : cards) {
						if (card.getValue() == solution.get(0)) {
							cardToActivate = card;
							break;
						}
					}
					mouseHandler.swapCards(cardToActivate);
				}

			}
		});

		newGameButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				flipSolverService.cancel();
				initCards();
				
			}
		});

		solveButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				flipSolverService.setCardState(cards);
				flipSolverService.restart();
			}
		});

		extendedVersion.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				mouseHandler.extendedEnabled(extendedVersion.isSelected());
				solveButton.setDisable(extendedVersion.isSelected());
			}
		});

		undoButon.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				flipSolverService.cancel();
				undoManager.undoLastAction();
			}
		});

		undoManager.undoSizeProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				undoButon.setDisable(newValue.intValue() == 0);
			}
		});

	}

	private Region createBackground() {
		Region region = new Region();
		region.getStyleClass().add("background");
		region.setPrefHeight(APP_HEIGHT);
		region.setPrefWidth(APP_WIDTH);
		return region;
	}

	private void initCards() {
		root.getChildren().removeAll(cards);
		cards.clear();
		final List<Integer> init = Arrays.asList(4, 6, 2, 9, 1, 8, 5, 7, 3);
		Collections.shuffle(init);
		for (int i = 0; i < Constants.GAME_CARDS; i++) {
			CardUi card = new CardUi(init.get(i));
			card.setLayoutX(i * 85 + 10);
			card.setLayoutY(200);
			cards.add(card);
			card.setCache(true);
			card.setCacheHint(CacheHint.SPEED);
		}
		root.getChildren().addAll(cards);

	}

	public static void main(String[] args) throws Exception {
		launch(args);
	}

}
