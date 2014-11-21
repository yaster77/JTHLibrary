package se.hj.doelibs.mobile.utils;

import java.util.List;

/**
 * helperclass to work with List<T>
 *
 * @author Christoph
 */
public class ListUtils {

	/**
	 * puts all elements of the list together to a string and puts the given "glue" between
	 * @param list all elements
	 * @param glue the String which should occur between the arraypieces
	 * @param <T> type of elements in the list
	 * @return String
	 */
	public static <T> String implode(List<T> list, String glue) {
		StringBuilder sb = new StringBuilder();

		int listsize = list.size();
		for(int i = 0;i<listsize;i++) {
			sb.append(list.get(i));

			//do not append the glue behind the last peace
			if(i < listsize-1) {
				sb.append(glue);
			}
		}

		return sb.toString();
	}
}
