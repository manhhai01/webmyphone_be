package com.project.webmyphone.webmyphone.service;

import com.project.webmyphone.webmyphone.common.Convert;
import com.project.webmyphone.webmyphone.dto.UserDTO;
import com.project.webmyphone.webmyphone.entity.User;
import com.project.webmyphone.webmyphone.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    Convert convert;

    @Autowired
    UserRepository userRepository;

    public int checkLogin(UserDTO uDTO) {
        uDTO.setPassword(DigestUtils.sha256Hex(uDTO.getPassword()));
        List<User> list = userRepository.findAll();
        for (User u : list) {
            if (StringUtils.equals(uDTO.getEmail(), u.getEmail())
                    && StringUtils.equals(uDTO.getPassword(), u.getPassword())) {
                if (u.getTrangthai() == 1) {
                    return u.getQuyen();
                }
                return -2;
            }
        }
        return -1;
    }

    public int checkRoleFromId(long mataikhoan) {
        User u = userRepository.findById(mataikhoan).get();
        return u.getQuyen();
    }

    public UserDTO loadUserDTOByEmail(String email) {
        List<User> list = userRepository.findAll();
        for (User u : list) {
            if (StringUtils.equals(email, u.getEmail())) {
                UserDTO uDTO = new UserDTO();
                uDTO.setMataikhoan(u.getMataikhoan());
                uDTO.setEmail(u.getEmail());
                uDTO.setPassword(u.getPassword());
                uDTO.setQuyen(u.getQuyen());
                return uDTO;
            }
        }
        return null;
    }

    public UserDTO getUserDetail(long mataikhoan) {
        User u = userRepository.findById(mataikhoan).get();
        UserDTO uDTO = new UserDTO();
        uDTO.setMataikhoan(u.getMataikhoan());
        uDTO.setEmail(u.getEmail());
        uDTO.setHolot(u.getHolot());
        uDTO.setTen(u.getTen());
        uDTO.setSodienthoai(u.getSodienthoai());
        uDTO.setQuyen(u.getQuyen());
        return uDTO;
    }

    public long getIdFromEmail(String email) {
        List<User> list = userRepository.findAll();
        for (User u : list) {
            if (StringUtils.equals(email, u.getEmail())) {
                return u.getMataikhoan();
            }
        }
        return -1;
    }

    public List<UserDTO> parseDTO(List<User> list) {
        List<UserDTO> listDTO = new ArrayList<>();
        UserDTO uDTO;
        for (User u : list) {
            uDTO = new UserDTO();
            uDTO.setMataikhoan(u.getMataikhoan());
            uDTO.setEmail(u.getEmail());
            uDTO.setHolot(u.getHolot());
            uDTO.setTen(u.getTen());
            uDTO.setSodienthoai(u.getSodienthoai());
            uDTO.setQuyen(u.getQuyen());
            listDTO.add(uDTO);
        }
        return listDTO;
    }

    public List<User> getListAccount(int filter, int status) {
        List<User> list = userRepository.findAll();
        List<User> listAccount = new ArrayList<>();
        if (filter == 0) {
            for (User u : list) {
                if (u.getTrangthai() == status) {
                    listAccount.add(u);
                }
            }
            if (status == 1) {
                listAccount.remove(0);
            }
            return listAccount;
        } else {
            for (User u : list) {
                if (u.getQuyen() == filter && u.getTrangthai() == status) {
                    listAccount.add(u);
                }
            }
            return listAccount;
        }
    }

    public Convert getListUser(int page, int filter, int status) {
        List<User> list = getListAccount(filter, status);
        convert = convert.userPage(list, page);
        List<User> listPage = Convert.typesafeAdd(convert.getListData(), new ArrayList<User>(), User.class);
        List<UserDTO> listDTO = parseDTO(listPage);
        convert.setListData(listDTO);
        return convert;
    }

    public boolean changePassword(long mataikhoan, String password, String newPassword) {
        password = DigestUtils.sha256Hex(password);
        User u = userRepository.findById(mataikhoan).get();
        if (StringUtils.equals(u.getPassword(), password) == false) {
            return false;
        }
        u.setPassword(DigestUtils.sha256Hex(newPassword));
        userRepository.save(u);
        return true;
    }

    public boolean createUser(UserDTO uDTO) {
        List<User> list = userRepository.findAll();
        for (User u : list) {
            if (StringUtils.equals(uDTO.getEmail(), u.getEmail())
                    || StringUtils.equals(uDTO.getSodienthoai(), u.getSodienthoai())) {
                return false;
            }
        }
        User u = new User();
        u.setEmail(uDTO.getEmail());
        u.setPassword(DigestUtils.sha256Hex(uDTO.getPassword()));
        u.setHolot(uDTO.getHolot());
        u.setTen(uDTO.getTen());
        u.setSodienthoai(uDTO.getSodienthoai());
        u.setQuyen(2);
        u.setTrangthai(1);
        userRepository.save(u);
        return true;
    }

    public boolean createStaff(UserDTO uDTO) {
        List<User> list = userRepository.findAll();
        for (User u : list) {
            if (StringUtils.equals(uDTO.getEmail(), u.getEmail())
                    || StringUtils.equals(uDTO.getSodienthoai(), u.getSodienthoai())) {
                return false;
            }
        }
        User u = new User();
        u.setEmail(uDTO.getEmail());
        u.setPassword(DigestUtils.sha256Hex(uDTO.getPassword()));
        u.setHolot(uDTO.getHolot());
        u.setTen(uDTO.getTen());
        u.setSodienthoai(uDTO.getSodienthoai());
        u.setQuyen(1);
        u.setTrangthai(1);
        userRepository.save(u);
        return true;
    }

    public boolean updateAccount(long mataikhoan, UserDTO uDTO) {
        List<User> list = userRepository.findAll();
        User user = userRepository.findById(mataikhoan).get();
        list.remove(user);
        for (User u : list) {
            if (StringUtils.equals(u.getSodienthoai(), uDTO.getSodienthoai())) {
                return false;
            }
        }
        user.setHolot(uDTO.getHolot());
        user.setTen(uDTO.getTen());
        user.setSodienthoai(uDTO.getSodienthoai());
        userRepository.save(user);
        return true;
    }

    public void lockAccount(long mataikhoan) {
        User u = userRepository.findById(mataikhoan).get();
        u.setTrangthai(0);
        userRepository.save(u);
    }


}
