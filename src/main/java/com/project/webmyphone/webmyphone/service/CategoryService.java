package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.dto.CategoryDTO;
import com.project.webmyphone.webmyphone.entity.Category;
import com.project.webmyphone.webmyphone.repository.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    Convert convert;

    @Autowired
    CategoryRepository categoryRepository;

    public List<Category> getListCategory(int status) {
        List<Category> list = categoryRepository.findAll();
        List<Category> listCate = new ArrayList<>();
        for (Category c : list) {
            if (c.getTrangthai() == status) {
                listCate.add(c);
            }
        }
        return listCate;
    }

    public List<CategoryDTO> parseDTO(List<Category> list) {
        List<CategoryDTO> listDTO = new ArrayList<>();
        CategoryDTO cDTO;
        for (Category c : list) {
            cDTO = new CategoryDTO();
            cDTO.setMadanhmuc(c.getMadanhmuc());
            cDTO.setTendanhmuc(c.getTendanhmuc());
            listDTO.add(cDTO);
        }
        return listDTO;
    }

    public Convert getListCategory(int quyen, int page, int status) {
        List<Category> list = new ArrayList<>();
        if (quyen == 0 || quyen == 1) {
            list = getListCategory(status);
            convert = convert.categoryPage(list, page);
            List<Category> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<Category>(),
                    Category.class);
            List<CategoryDTO> listDTO = parseDTO(listPage);
            convert.setListData(listDTO);
        } else if (quyen == 2) {
            list = getListCategory(1);
            List<CategoryDTO> listDTO = parseDTO(list);
            convert.setListData(listDTO);
        }
        return convert;
    }

    public CategoryDTO getCategoryById(long madanhmuc) {
        Category c = categoryRepository.findById(madanhmuc).get();
        CategoryDTO cDTO = new CategoryDTO();
        cDTO.setMadanhmuc(c.getMadanhmuc());
        cDTO.setTendanhmuc(c.getTendanhmuc());
        return cDTO;
    }

    public boolean updateCategory(long madanhmuc, CategoryDTO cDTO) {
        Category category = categoryRepository.findById(madanhmuc).get();
        List<Category> list = categoryRepository.findAll();
        list.remove(category);
        for (Category c : list) {
            if (StringUtils.equals(c.getTendanhmuc(), cDTO.getTendanhmuc())) {
                return false;
            }
        }
        category.setTendanhmuc(cDTO.getTendanhmuc());
        categoryRepository.save(category);
        return true;
    }

    public boolean addCategory(CategoryDTO cDTO) {
        List<Category> list = categoryRepository.findAll();
        for (Category c : list) {
            if (StringUtils.equals(c.getTendanhmuc(), cDTO.getTendanhmuc())) {
                return false;
            }
        }
        Category c = new Category();
        c.setTendanhmuc(cDTO.getTendanhmuc());
        c.setTrangthai(1);
        categoryRepository.save(c);
        return true;
    }

    public void deleteCategory(long madanhmuc) {
        Category category = categoryRepository.findById(madanhmuc).get();
        category.setTrangthai(0);
        categoryRepository.save(category);
    }
}
