package com.project.webmyphone.webmyphone.common;

import com.project.webmyphone.webmyphone.entity.*;
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

    public Convert productPage(int quantity, List<Product> list, int page) {
        Convert convert = new Convert();
        Map<String, Integer> map = new HashMap<>();
        int size = list.size();
        int start = ((page - 1) * quantity) + 1;
        int end = page * quantity;
        int totalPage = (int) Math.ceil((double) size / quantity);
        map.put("totalProducts", size);
        map.put("totalPages", totalPage);
        map.put("currentPage", page);
        map.put("countInPage", quantity);
        convert.setMapData(map);
        List<Product> productList = new ArrayList<>();
        for (int i = start - 1; i < end; i++) {
            if (list.size() - 1 < i) {
                convert.setListData(productList);
                return convert;
            }
            Product p = list.get(i);
            productList.add(p);
        }
        convert.setListData(productList);
        return convert;
    }

    public Convert categoryPage(List<Category> list, int page) {
        Convert convert = new Convert();
        Map<String, Integer> map = new HashMap<>();
        int size = list.size();
        int quantity = 10;
        int start = ((page - 1) * quantity) + 1;
        int end = page * quantity;
        int totalPage = (int) Math.ceil((double) size / quantity);
        map.put("totalCategorys", size);
        map.put("totalPages", totalPage);
        map.put("currentPage", page);
        map.put("countInPage", quantity);
        convert.setMapData(map);
        List<Category> categoryList = new ArrayList<>();
        for (int i = start - 1; i < end; i++) {
            if (list.size() - 1 < i) {
                convert.setListData(categoryList);
                return convert;
            }
            Category c = list.get(i);
            categoryList.add(c);
        }
        convert.setListData(categoryList);
        return convert;
    }

    public Convert trademarkPage(List<Trademark> list, int page) {
        Convert convert = new Convert();
        Map<String, Integer> map = new HashMap<>();
        int size = list.size();
        int quantity = 10;
        int start = ((page - 1) * quantity) + 1;
        int end = page * quantity;
        int totalPage = (int) Math.ceil((double) size / quantity);
        map.put("totalTrademarks", size);
        map.put("totalPages", totalPage);
        map.put("currentPage", page);
        map.put("countInPage", quantity);
        convert.setMapData(map);
        List<Trademark> trademarkList = new ArrayList<>();
        for (int i = start - 1; i < end; i++) {
            if (list.size() - 1 < i) {
                convert.setListData(trademarkList);
                return convert;
            }
            Trademark t = list.get(i);
            trademarkList.add(t);
        }
        convert.setListData(trademarkList);
        return convert;
    }

    public Convert orderPage(List<Order> list, int page) {
        Convert convert = new Convert();
        Map<String, Integer> map = new HashMap<>();
        int size = list.size();
        int quantity = 10;
        int start = ((page - 1) * quantity) + 1;
        int end = page * quantity;
        int totalPage = (int) Math.ceil((double) size / quantity);
        map.put("totalOrders", size);
        map.put("totalPages", totalPage);
        map.put("currentPage", page);
        map.put("countInPage", quantity);
        convert.setMapData(map);
        List<Order> orderList = new ArrayList<>();
        for (int i = start - 1; i < end; i++) {
            if (list.size() - 1 < i) {
                convert.setListData(orderList);
                return convert;
            }
            Order o = list.get(i);
            orderList.add(o);
        }
        convert.setListData(orderList);
        return convert;
    }

    public Convert importPage(List<ImportGoods> list, int page) {
        Convert convert = new Convert();
        Map<String, Integer> map = new HashMap<>();
        int size = list.size();
        int quantity = 10;
        int start = ((page - 1) * quantity) + 1;
        int end = page * quantity;
        int totalPage = (int) Math.ceil((double) size / quantity);
        map.put("totalImports", size);
        map.put("totalPages", totalPage);
        map.put("currentPage", page);
        map.put("countInPage", quantity);
        convert.setMapData(map);
        List<ImportGoods> importList = new ArrayList<>();
        for (int i = start - 1; i < end; i++) {
            if (list.size() - 1 < i) {
                convert.setListData(importList);
                return convert;
            }
            ImportGoods ig = list.get(i);
            importList.add(ig);
        }
        convert.setListData(importList);
        return convert;
    }

    public static <T, C extends Collection<T>> C typesafeAdd(Iterable<?> from, C to, Class<T> listClass) {
        for (Object item : from) {
            to.add(listClass.cast(item));
        }
        return to;
    }
}
