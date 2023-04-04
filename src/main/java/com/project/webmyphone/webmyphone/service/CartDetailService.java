package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.dto.CartDetailDTO;
import com.project.webmyphone.webmyphone.entity.CartDetail;
import com.project.webmyphone.webmyphone.entity.Product;
import com.project.webmyphone.webmyphone.repository.CartDetailRepository;
import com.project.webmyphone.webmyphone.repository.CartRepository;
import com.project.webmyphone.webmyphone.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartDetailService {

    @Autowired
    CartDetailRepository cartDetailRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageService imageService;

    public CartDetail checkExistProduct(long magiohang, long masanpham) {
        List<CartDetail> list = cartDetailRepository.findAll();
        for (CartDetail cd : list) {
            if (cd.getGiohang().getMagiohang() == magiohang && cd.getSanpham().getMasanpham() == masanpham) {
                return cd;
            }
        }
        return null;
    }

    public int checkSoluong(long magiohang) {
        List<CartDetail> list = cartDetailRepository.findAll();
        int soluong = 0;
        for (CartDetail cd : list) {
            if (cd.getGiohang().getMagiohang() == magiohang) {
                soluong += cd.getSoluong();
            }
        }
        return soluong;
    }

    public void addCartDetail(long magiohang, long masanpham) {
        CartDetail cd = checkExistProduct(magiohang, masanpham);
        if (cd != null) {
            cd.setSoluong(cd.getSoluong() + 1);
            cd.setThanhtien(cd.getSoluong() * cd.getDongia());
            cartDetailRepository.save(cd);
        } else {
            cd = new CartDetail();
            Product p = productRepository.findById(masanpham).get();
            cd.setGiohang(cartRepository.findById(magiohang).get());
            cd.setSanpham(p);
            cd.setSoluong(1);
            if (p.getGiamgia() == 0) {
                cd.setDongia(p.getGia());
                cd.setThanhtien(cd.getSoluong() * p.getGia());
            } else {
                cd.setDongia(p.getGiamgia());
                cd.setThanhtien(cd.getSoluong() * p.getGiamgia());
            }
            cartDetailRepository.save(cd);
        }
    }

    public List<CartDetailDTO> getListDetail(long magiohang) {
        List<CartDetail> list = cartDetailRepository.findAll();
        List<CartDetailDTO> listDTO = new ArrayList<>();
        CartDetailDTO cartDetailDTO;
        for (CartDetail cd : list) {
            if (cd.getGiohang().getMagiohang() == magiohang) {
                cartDetailDTO = new CartDetailDTO();
                Product p = productRepository.findById(cd.getSanpham().getMasanpham()).get();
                cartDetailDTO.setMasanpham(cd.getSanpham().getMasanpham());
                cartDetailDTO.setTensanpham(p.getTensanpham());
                cartDetailDTO.setAnh(imageService.getImageIndex(p.getMasanpham()));
                cartDetailDTO.setSoluong(cd.getSoluong());
                cartDetailDTO.setDongia(cd.getDongia());
                cartDetailDTO.setThanhtien(cd.getThanhtien());
                cartDetailDTO
                        .setTinhtrang(productRepository.findById(cd.getSanpham().getMasanpham()).get().getTrangthai());
                listDTO.add(cartDetailDTO);
            }
        }
        return listDTO;
    }

    public void deleteCartDetail(long magiohang) {
        List<CartDetail> list = cartDetailRepository.findAll();
        for (CartDetail cd : list) {
            if (cd.getGiohang().getMagiohang() == magiohang) {
                cartDetailRepository.delete(cd);
            }
        }
    }

    public String deleteOneProduct(long magiohang, long masanpham) {
        List<CartDetail> list = cartDetailRepository.findAll();
        for (CartDetail cd : list) {
            if (cd.getGiohang().getMagiohang() == magiohang && cd.getSanpham().getMasanpham() == masanpham) {
                String name = cd.getSanpham().getTensanpham();
                cartDetailRepository.delete(cd);
                return name;
            }
        }
        return null;
    }

    public long updateCart(long magiohang, List<CartDetailDTO> listDTO) {
        List<CartDetail> list = cartDetailRepository.findAll();
        long tongtien = 0;
        for (CartDetail cd : list) {
            if (cd.getGiohang().getMagiohang() == magiohang) {
                for (CartDetailDTO cdDTO : listDTO) {
                    if (cd.getSanpham().getMasanpham() == cdDTO.getMasanpham()) {
                        cd.setSoluong(cdDTO.getSoluong());
                        cd.setThanhtien(cdDTO.getThanhtien());
                        tongtien += cd.getThanhtien();
                        cartDetailRepository.save(cd);
                    }
                }
            }
        }
        return tongtien;
    }
}
