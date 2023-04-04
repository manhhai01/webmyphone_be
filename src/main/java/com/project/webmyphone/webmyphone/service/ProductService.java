package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.dto.ProductDTO;
import com.project.webmyphone.webmyphone.entity.Product;
import com.project.webmyphone.webmyphone.repository.CategoryRepository;
import com.project.webmyphone.webmyphone.repository.ProductRepository;
import com.project.webmyphone.webmyphone.repository.TrademarkRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    Convert convert;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ImageService imageService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    TrademarkRepository trademarkRepository;

    public List<Product> getListProductbySort(int cate, int trade, int status) {
        List<Product> list = productRepository.findAll();
        List<Product> listSort = new ArrayList<>();
        for (Product p : list) {
            if (p.getTrangthai() == status) {
                if (cate == 0 && trade == 0) {
                    return list;
                } else if (cate != 0 && trade == 0) {
                    if (p.getDanhmuc().getMadanhmuc() == cate) {
                        listSort.add(p);
                    }
                } else if (cate == 0 && trade != 0) {
                    if (p.getThuonghieu().getMathuonghieu() == trade) {
                        listSort.add(p);
                    }
                } else if (cate != 0 && trade != 0) {
                    if (p.getDanhmuc().getMadanhmuc() == cate && p.getThuonghieu().getMathuonghieu() == trade) {
                        listSort.add(p);
                    }
                }
            }
        }
        return listSort;
    }

    public List<ProductDTO> parseDTO(List<Product> list) {
        List<ProductDTO> listDTO = new ArrayList<>();
        ProductDTO pDTO;
        for (Product p : list) {
            pDTO = new ProductDTO();
            pDTO.setMasanpham(p.getMasanpham());
            pDTO.setTensanpham(p.getTensanpham());
            pDTO.setMota(p.getMota());
            pDTO.setSoluong(p.getSoluong());
            pDTO.setAnh(imageService.getImageIndex(p.getMasanpham()));
            if (p.getGiamgia() == 0) {
                pDTO.setGiamgia(p.getGia());
            } else {
                pDTO.setGia(p.getGia());
                pDTO.setGiamgia(p.getGiamgia());
            }
            pDTO.setMathuonghieu(p.getThuonghieu().getMathuonghieu());
            pDTO.setTenthuonghieu(p.getThuonghieu().getTenthuonghieu());
            pDTO.setMadanhmuc(p.getDanhmuc().getMadanhmuc());
            pDTO.setTendanhmuc(p.getDanhmuc().getTendanhmuc());
            listDTO.add(pDTO);
        }
        return listDTO;
    }

    public Convert getListProduct(int quyen, int page, int cate, int trade, int status) {
        List<Product> list = new ArrayList<>();
        if (quyen == 0 || quyen == 1) {
            list = getListProductbySort(cate, trade, status);
            convert = convert.productPage(10, list, page);
        } else if (quyen == 2) {
            list = getListProductbySort(cate, trade, 1);
            convert = convert.productPage(12, list, page);
        }
        List<Product> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<Product>(), Product.class);
        List<ProductDTO> listDTO = parseDTO(listPage);
        convert.setListData(listDTO);
        return convert;
    }

    public ProductDTO getProductDetail(long masanpham) {
        Product p = productRepository.findById(masanpham).get();
        ProductDTO pDTO = new ProductDTO();
        pDTO.setMasanpham(p.getMasanpham());
        pDTO.setTensanpham(p.getTensanpham());
        pDTO.setMadanhmuc(p.getDanhmuc().getMadanhmuc());
        pDTO.setTendanhmuc(p.getDanhmuc().getTendanhmuc());
        pDTO.setMathuonghieu(p.getThuonghieu().getMathuonghieu());
        pDTO.setTenthuonghieu(p.getThuonghieu().getTenthuonghieu());
        if (p.getGiamgia() == 0) {
            pDTO.setGiamgia(p.getGia());
        } else {
            pDTO.setGia(p.getGia());
            pDTO.setGiamgia(p.getGiamgia());
        }
        // pDTO.setAnh(imageService.getListImageDetail(p.getMasanpham()));
        pDTO.setAnh(imageService.getImageIndex(p.getMasanpham()));
        pDTO.setMota(p.getMota());
        return pDTO;
    }

    public boolean addProduct(ProductDTO pDTO) {
        List<Product> list = productRepository.findAll();
        for (Product p : list) {
            if (StringUtils.equals(p.getTensanpham(), pDTO.getTensanpham())) {
                return false;
            }
        }
        Product p = new Product();
        p.setTensanpham(pDTO.getTensanpham());
        p.setDanhmuc(categoryRepository.findById(pDTO.getMadanhmuc()).get());
        p.setThuonghieu(trademarkRepository.findById(pDTO.getMathuonghieu()).get());
        p.setSoluong(0);
        p.setMota(pDTO.getMota());
        p.setGia(pDTO.getGia());
        p.setGiamgia(pDTO.getGiamgia());
        p.setTrangthai(1);
        productRepository.save(p);
        imageService.addImage(pDTO.getAnh(), p.getMasanpham());
        return true;
    }

    public void deleteProduct(long masanpham) {
        Product p = productRepository.findById(masanpham).get();
        p.setTrangthai(0);
        productRepository.save(p);
    }

    public boolean updateProduct(long masanpham, ProductDTO pDTO) {
        List<Product> list = productRepository.findAll();
        int i = 0;
        for (Product p : list) {
            if (p.getMasanpham() != masanpham) {
                i++;
            } else if (p.getMasanpham() == masanpham) {
                break;
            }
        }
        list.remove(i);
        for (Product p : list) {
            if (StringUtils.equals(p.getTensanpham(), pDTO.getTensanpham())) {
                return false;
            }
        }
        Product p = productRepository.findById(masanpham).get();
        p.setTensanpham(pDTO.getTensanpham());
        p.setDanhmuc(categoryRepository.findById(pDTO.getMadanhmuc()).get());
        p.setThuonghieu(trademarkRepository.findById(pDTO.getMathuonghieu()).get());
        p.setMota(pDTO.getMota());
        p.setGia(pDTO.getGia());
        p.setGiamgia(pDTO.getGiamgia());
        imageService.updateImage(pDTO.getAnh(), masanpham);
        productRepository.save(p);
        return true;
    }
}
