package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.dto.ImportGoodsDetailDTO;
import com.project.webmyphone.webmyphone.entity.ImportGoodsDetail;
import com.project.webmyphone.webmyphone.entity.Product;
import com.project.webmyphone.webmyphone.repository.ImportGoodsDetailRepository;
import com.project.webmyphone.webmyphone.repository.ImportGoodsRepository;
import com.project.webmyphone.webmyphone.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportGoodsDetailService {
    @Autowired
    ImportGoodsDetailRepository importDetailRepository;

    @Autowired
    ImportGoodsRepository importRepository;

    @Autowired
    ProductRepository productRepository;

    public List<ImportGoodsDetailDTO> getListImportGoodsDetail(long maphieunhap) {
        List<ImportGoodsDetail> list = importDetailRepository.findAll();
        List<ImportGoodsDetailDTO> listDTO = new ArrayList<>();
        ImportGoodsDetailDTO igDTO;
        for (ImportGoodsDetail ig : list) {
            igDTO = new ImportGoodsDetailDTO();
            if (ig.getPhieunhap().getMaphieunhap() == maphieunhap) {
                igDTO.setMasanpham(ig.getSanpham().getMasanpham());
                igDTO.setTensanpham(ig.getSanpham().getTensanpham());
                igDTO.setSoluong(ig.getSoluong());
                igDTO.setDongia(ig.getDongia());
                igDTO.setThanhtien(ig.getThanhtien());
                listDTO.add(igDTO);
            }
        }
        return listDTO;
    }

    public void addImportDetail(long maphieunhap, List<ImportGoodsDetailDTO> listDTO) {
        ImportGoodsDetail ig;
        Product p;
        for (ImportGoodsDetailDTO igDTO : listDTO) {
            ig = new ImportGoodsDetail();
            ig.setPhieunhap(importRepository.findById(maphieunhap).get());
            p = productRepository.findById(igDTO.getMasanpham()).get();
            ig.setSanpham(p);
            ig.setSoluong(igDTO.getSoluong());
            ig.setDongia(igDTO.getDongia());
            ig.setThanhtien(igDTO.getThanhtien());
            importDetailRepository.save(ig);
            p.setSoluong(p.getSoluong() + ig.getSoluong());
            productRepository.save(p);
        }
    }
}
