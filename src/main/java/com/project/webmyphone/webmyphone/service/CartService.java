package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.dto.CartDTO;
import com.project.webmyphone.webmyphone.entity.Cart;
import com.project.webmyphone.webmyphone.entity.Product;
import com.project.webmyphone.webmyphone.entity.User;
import com.project.webmyphone.webmyphone.repository.CartRepository;
import com.project.webmyphone.webmyphone.repository.ProductRepository;
import com.project.webmyphone.webmyphone.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CartDetailService cartDetailService;

    public Cart checkExistCart(long mataikhoan) {
        List<Cart> list = cartRepository.findAll();
        for (Cart cart : list) {
            if (cart.getTaikhoan().getMataikhoan() == mataikhoan) {
                return cart;
            }
        }
        return null;
    }

    public void addCart(long mataikhoan, long masanpham) {
        Cart cart = checkExistCart(mataikhoan);
        Product p = productRepository.findById(masanpham).get();
        if (cart != null) {
            if (p.getGiamgia() == 0) {
                cart.setTongtien(cart.getTongtien() + p.getGia());
            } else {
                cart.setTongtien(cart.getTongtien() + p.getGiamgia());
            }
            cartRepository.save(cart);
            cartDetailService.addCartDetail(cart.getMagiohang(), masanpham);
        } else {
            cart = new Cart();
            cart.setTaikhoan(userRepository.findById(mataikhoan).get());
            if (p.getGiamgia() == 0) {
                cart.setTongtien(p.getGia());
            } else {
                cart.setTongtien(p.getGiamgia());
            }
            cartRepository.save(cart);
            cartDetailService.addCartDetail(cart.getMagiohang(), masanpham);
        }
    }

    public CartDTO getCartDTO(long mataikhoan) {
        List<Cart> list = cartRepository.findAll();
        CartDTO cDTO = new CartDTO();
        for (Cart c : list) {
            if (c.getTaikhoan().getMataikhoan() == mataikhoan) {
                cDTO = new CartDTO();
                cDTO.setMataikhoan(mataikhoan);
                User u = userRepository.findById(mataikhoan).get();
                cDTO.setTenkhachhang(u.getHolot() + " " + u.getTen());
                cDTO.setTongtien(c.getTongtien());
                cDTO.setChitietgiohang(cartDetailService.getListDetail(c.getMagiohang()));
                return cDTO;
            }
        }
        return null;
    }

    public Cart getCart(long mataikhoan) {
        List<Cart> list = cartRepository.findAll();
        for (Cart c : list) {
            if (c.getTaikhoan().getMataikhoan() == mataikhoan) {
                return c;
            }
        }
        return null;
    }

    public void deleteCart(long mataikhoan) {
        List<Cart> list = cartRepository.findAll();
        for (Cart c : list) {
            if (c.getTaikhoan().getMataikhoan() == mataikhoan) {
                cartDetailService.deleteCartDetail(c.getMagiohang());
                cartRepository.delete(c);
            }
        }
    }

    public String deleteOneProduct(long mataikhoan, long masanpham) {
        List<Cart> list = cartRepository.findAll();
        for (Cart c : list) {
            if (c.getTaikhoan().getMataikhoan() == mataikhoan) {
                return cartDetailService.deleteOneProduct(c.getMagiohang(), masanpham);
            }
        }
        return null;
    }

    public void updateCart(long mataikhoan, CartDTO cDTO) {
        List<Cart> list = cartRepository.findAll();
        for (Cart c : list) {
            if (c.getTaikhoan().getMataikhoan() == mataikhoan) {
                long tongtien = cartDetailService.updateCart(c.getMagiohang(),
                        cDTO.getChitietgiohang());
                c.setTongtien(tongtien);
                cartRepository.save(c);
            }
        }
    }
}
