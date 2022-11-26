package com.example.team_soa.utils;

import javafx.util.Pair;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class Utils {
    public static Pair<Integer, Integer> get_pagination(Integer page, Integer size) {
        if (size == null) {
            size = Integer.MAX_VALUE;
            page = 0;
        }
        if (page == null) {
            page = 0;
        }
        Pair<Integer, Integer> ps = new Pair<>(page, size);
        return ps;
    }

    public static Pageable getPageable(Integer page, Integer size, String sort, String order) {
        Pair<Integer, Integer> page_size = get_pagination(page, size);
        Pageable pageable;
        if (sort != null) {
            if (order == null) {
                order = "asc";
            }
            if (order.equals("desc")) {
                pageable = PageRequest.of(page_size.getKey(), page_size.getValue(), Sort.by(sort).descending());
            } else {
                pageable = PageRequest.of(page_size.getKey(), page_size.getValue(), Sort.by(sort).ascending());
            }
        } else {
            pageable = PageRequest.of(page_size.getKey(), page_size.getValue());
        }
        return pageable;
    }

    public static <T> T checkNull(T curValue, T value) {
        if (curValue == null) {
            return value;
        }
        return curValue;
    }

}
