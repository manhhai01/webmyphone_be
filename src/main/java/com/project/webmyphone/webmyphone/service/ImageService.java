package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.dto.ImageDTO;
import com.project.webmyphone.webmyphone.entity.Image;
import com.project.webmyphone.webmyphone.repository.ImageRepository;
import com.project.webmyphone.webmyphone.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProductRepository productRepository;

    public List<ImageDTO> getImageIndex(long masanpham) {
        List<Image> listImage = imageRepository.findAll();
        List<ImageDTO> listDTO = new ArrayList<>();
        ImageDTO iDTO;
        for (Image i : listImage) {
            iDTO = new ImageDTO();
            if (i.getSanpham().getMasanpham() == masanpham) {
                if (i.getVitri() == 1) {
                    iDTO.setMahinhanh(i.getMahinhanh());
                    iDTO.setTenhinhanh(i.getSanpham().getDanhmuc().getTendanhmuc() + "/" + i.getTenhinhanh());
                    iDTO.setVitri(i.getVitri());
                    listDTO.add(iDTO);
                }
            }
        }
        return listDTO;
    }

    public List<ImageDTO> getListImageDetail(long masanpham) {
        List<Image> listImage = imageRepository.findAll();
        List<ImageDTO> listDTO = new ArrayList<>();
        ImageDTO iDTO;
        for (Image i : listImage) {
            iDTO = new ImageDTO();
            if (i.getSanpham().getMasanpham() == masanpham) {
                iDTO.setMahinhanh(i.getMahinhanh());
                iDTO.setTenhinhanh(i.getSanpham().getDanhmuc().getTendanhmuc() + "/" + i.getTenhinhanh());
                iDTO.setVitri(i.getVitri());
                listDTO.add(iDTO);
            }
        }
        return listDTO;
    }

    public void addImage(List<ImageDTO> listDTO, long masanpham) {
        Image i;
        for (ImageDTO iDTO : listDTO) {
            i = new Image();
            i.setTenhinhanh(iDTO.getTenhinhanh());
            i.setVitri(iDTO.getVitri());
            i.setSanpham(productRepository.findById(masanpham).get());
            imageRepository.save(i);
        }
    }

    public void updateImage(List<ImageDTO> listDTO, long masanpham) {
        List<Image> list = imageRepository.findAll();
        List<Image> listImage = new ArrayList<>();
        for (Image i : list) {
            if (i.getSanpham().getMasanpham() == masanpham) {
                listImage.add(i);
            }
        }
        for (ImageDTO iDTO : listDTO) {
            for (Image i : listImage) {
                if (iDTO.getVitri() == i.getVitri()) {
                    Image img = imageRepository.findById(i.getMahinhanh()).get();
                    img.setTenhinhanh(iDTO.getTenhinhanh());
                    imageRepository.save(img);
                }
            }
        }
    }
}
