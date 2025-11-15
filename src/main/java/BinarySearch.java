import java.util.List;

public class BinarySearch{

    public static <T extends Comparable<T>> int search(List<T> list, T target) {
        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int middle = (left + right) / 2;
            if (list.get(middle).equals(target)) return middle;
            if (list.get(middle).compareTo(target) < 0) left = middle + 1;
            else right = middle - 1;
        }
        return -1;
    }
}
