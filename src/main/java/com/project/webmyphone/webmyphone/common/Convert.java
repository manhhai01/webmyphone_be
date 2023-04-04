package com.project.webmyphone.webmyphone.common;

import com.project.webmyphone.webmyphone.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
public class Convert {

    public List<?> listData;
    public Map<String, ?> mapData;

    public Convert userPage(List<User> list, int page) {
        Convert convert = new Convert();
        Map<String, Integer> map = new HashMap<>();
        int size = list.size();
        int quantity = 10;
        int start = ((page - 1) * quantity) + 1;
        int end = page * quantity;
        int totalPage = (int) Math.ceil((double) size / quantity);
        map.put("totalAccounts", size);
        map.put("totalPages", totalPage);
        map.put("currentPage", page);
        map.put("countInPage", quantity);
        convert.setMapData(map);
        List<User> userList = new ArrayList<>();
        for (int i = start - 1; i < end; i++) {
            if (list.size() - 1 < i) {
                convert.setListData(userList);
                return convert;
            }
            User u = list.get(i);
            userList.add(u);
        }
        convert.setListData(userList);
        return convert;
    }



    public static <T, C extends Collection<T>> C typesafeAdd(Iterable<?> from, C to, Class<T> listClass) {
        for (Object item : from) {
            to.add(listClass.cast(item));
        }
        return to;
    }
}
