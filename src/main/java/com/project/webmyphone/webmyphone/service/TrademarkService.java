package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.dto.TrademarkDTO;
import com.project.webmyphone.webmyphone.entity.Trademark;
import com.project.webmyphone.webmyphone.repository.TrademarkRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrademarkService {
    @Autowired
    Convert convert;

    @Autowired
    TrademarkRepository trademarkRepository;

    public List<Trademark> getListTrademark(int status) {
        List<Trademark> list = trademarkRepository.findAll();
        List<Trademark> listTrademark = new ArrayList<>();
        for (Trademark t : list) {
            if (t.getTrangthai() == status) {
                listTrademark.add(t);
            }
        }
        return listTrademark;
    }

    public List<TrademarkDTO> parseDTO(List<Trademark> list) {
        List<TrademarkDTO> listDTO = new ArrayList<>();
        TrademarkDTO tDTO;
        for (Trademark t : list) {
            tDTO = new TrademarkDTO();
            tDTO.setMathuonghieu(t.getMathuonghieu());
            tDTO.setTenthuonghieu(t.getTenthuonghieu());
            listDTO.add(tDTO);
        }
        return listDTO;
    }

    public Convert getListTrademark(int quyen, int page, int status) {
        List<Trademark> list = new ArrayList<>();
        if (quyen == 0 || quyen == 1) {
            list = getListTrademark(status);
            convert = convert.trademarkPage(list, page);
            List<Trademark> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<Trademark>(),
                    Trademark.class);
            List<TrademarkDTO> listDTO = parseDTO(listPage);
            convert.setListData(listDTO);
        } else if (quyen == 2) {
            list = getListTrademark(1);
            List<TrademarkDTO> listDTO = parseDTO(list);
            convert.setListData(listDTO);
        }
        return convert;
    }

    public TrademarkDTO getTrademarkById(long mathuonghieu) {
        Trademark t = trademarkRepository.findById(mathuonghieu).get();
        TrademarkDTO tDTO = new TrademarkDTO();
        tDTO.setMathuonghieu(t.getMathuonghieu());
        tDTO.setTenthuonghieu(t.getTenthuonghieu());
        return tDTO;
    }

    public boolean updateTrademark(long mathuonghieu, TrademarkDTO tDTO) {
        Trademark trademark = trademarkRepository.findById(mathuonghieu).get();
        List<Trademark> list = trademarkRepository.findAll();
        list.remove(trademark);
        for (Trademark t : list) {
            if (StringUtils.equals(t.getTenthuonghieu(), tDTO.getTenthuonghieu())) {
                return false;
            }
        }
        trademark.setTenthuonghieu(tDTO.getTenthuonghieu());
        trademarkRepository.save(trademark);
        return true;
    }

    public boolean addTrademark(TrademarkDTO tDTO) {
        List<Trademark> list = trademarkRepository.findAll();
        for (Trademark t : list) {
            if (StringUtils.equals(t.getTenthuonghieu(), tDTO.getTenthuonghieu())) {
                return false;
            }
        }
        Trademark t = new Trademark();
        t.setTenthuonghieu(tDTO.getTenthuonghieu());
        t.setTrangthai(1);
        trademarkRepository.save(t);
        return true;
    }

    public void deleteTrademark(long mathuonghieu) {
        Trademark t = trademarkRepository.findById(mathuonghieu).get();
        t.setTrangthai(0);
        trademarkRepository.save(t);
    }
}
