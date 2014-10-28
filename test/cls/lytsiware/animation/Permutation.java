package cls.lytsiware.animation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Permutation {
	
	private static <T> List<List<T>> permutation(List<T> initialList) {
		if (initialList.size() == 2) {
			List<List<T>> result = new ArrayList<>();
			result.add(new ArrayList<>(initialList));
			ArrayList<T> swapRes = new ArrayList<>(initialList);
			Collections.swap(swapRes, 0, 1);
			result.add(swapRes);
			return result;
		} else {
			List<List<T>> fresult = new ArrayList<>();
			for (int i = 0; i < initialList.size(); i++) {
				ArrayList<T> workingList = new ArrayList<>(initialList);
				if (i != 0) {
					Collections.swap(workingList, 0, i);
				}
				T item = workingList.remove(0);
				List<List<T>> result = permutation(workingList);
				for (List<T> sub : result) {
					sub.add(0, item);
				}
				fresult.addAll(result);
			}
			return fresult;
		}

	}

}
