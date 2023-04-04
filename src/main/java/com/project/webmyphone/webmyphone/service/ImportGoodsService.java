package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.dto.ImportGoodsDTO;
import com.project.webmyphone.webmyphone.entity.ImportGoods;
import com.project.webmyphone.webmyphone.repository.ImportGoodsRepository;
import com.project.webmyphone.webmyphone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ImportGoodsService {
    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Autowired
    Convert convert;

    @Autowired
    ImportGoodsRepository importRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ImportGoodsDetailService importDetailService;

    public List<ImportGoodsDTO> parseDTO(List<ImportGoods> list) {
        List<ImportGoodsDTO> listDTO = new ArrayList<>();
        ImportGoodsDTO igDTO;
        for (ImportGoods ig : list) {
            igDTO = new ImportGoodsDTO();
            igDTO.setMaphieunhap(ig.getMaphieunhap());
            igDTO.setManhanvien(ig.getNhanvien().getMataikhoan());
            igDTO.setTennhanvien(ig.getNhanvien().getHolot() + " " + ig.getNhanvien().getTen());
            igDTO.setNgaynhap(format.format(ig.getNgaynhap()));
            igDTO.setTongtien(ig.getTongtien());
            listDTO.add(igDTO);
        }
        return listDTO;
    }

    public Convert getListImport(int page) {
        List<ImportGoods> list = importRepository.findAll();
        convert = convert.importPage(list, page);
        List<ImportGoods> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<ImportGoods>(),
                ImportGoods.class);
        List<ImportGoodsDTO> listDTO = parseDTO(listPage);
        convert.setListData(listDTO);
        return convert;
    }

    public ImportGoodsDTO getImportDetail(long maphieunhap) {
        ImportGoods ig = importRepository.findById(maphieunhap).get();
        ImportGoodsDTO igDTO = new ImportGoodsDTO();
        igDTO.setMaphieunhap(ig.getMaphieunhap());
        igDTO.setManhanvien(ig.getNhanvien().getMataikhoan());
        igDTO.setTennhanvien(ig.getNhanvien().getHolot() + " " + ig.getNhanvien().getTen());
        igDTO.setChitietphieunhap(importDetailService.getListImportGoodsDetail(maphieunhap));
        igDTO.setNgaynhap(format.format(ig.getNgaynhap()));
        igDTO.setTongtien(ig.getTongtien());
        return igDTO;
    }

    public void addImportGoods(long mataikhoan, ImportGoodsDTO igDTO) {
        ImportGoods ig = new ImportGoods();
        ig.setNhanvien(userRepository.findById(mataikhoan).get());
        ig.setNgaynhap(new Date());
        ig.setTongtien(igDTO.getTongtien());
        importRepository.save(ig);
        importDetailService.addImportDetail(ig.getMaphieunhap(), igDTO.getChitietphieunhap());
    }
}
