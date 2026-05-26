package Room.Sorting;

import Room.RoomType;

import java.util.Comparator;

public class SortByPrice implements Comparator<RoomType>{

    public int compare(RoomType a, RoomType b)
    {
        return Double.compare(a.getBasePrice(), b.getBasePrice());
    }

}
