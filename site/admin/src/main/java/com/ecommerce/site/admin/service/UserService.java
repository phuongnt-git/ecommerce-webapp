package com.ecommerce.site.admin.service;

import com.ecommerce.common.exception.UserNotFoundException;
import com.ecommerce.common.model.entity.Role;
import com.ecommerce.common.model.entity.User;
import com.ecommerce.site.admin.helper.PagingAndSortingHelper;
import com.ecommerce.site.admin.repository.RoleRepository;
import com.ecommerce.site.admin.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ecommerce.site.admin.constant.ApplicationConstant.USERS_PER_PAGE;
/**
 * @author Nguyen Thanh Phuong
 */

@Service
@Transactional(rollbackFor = Exception.class)
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean checkUserExists(Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.isPresent();
    }

    public void updateFailedAttempts(@NotNull User user) {
        userRepository.updateFailedAttempts(user.getEmail(), new Date());
    }

    public void resetFailedAttempts(@NotNull User user) {
        userRepository.resetFailedAttempts(user.getEmail());
    }

    public void lock(@NotNull User user) {
        user.setAccountNonLocked(false);
        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(@NotNull User user, long timeDuration) {
        long lastModified = user.getLastModified().getTime();
        long currentTime = System.currentTimeMillis();

        if (lastModified + timeDuration < currentTime) {
            user.setAccountNonLocked(true);
            userRepository.save(user);

            return true;
        }
        return false;
    }


    public List<User> listAll() {
        return userRepository.findAll();
    }

    public void listByPage(int pageNumber, @NotNull PagingAndSortingHelper helper) {
        helper.listEntities(pageNumber, USERS_PER_PAGE, userRepository);
    }

    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    public User save(@NotNull User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepository.findById(user.getId()).get();


            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());

            } else {
                encodePassword(user);
            }

        } else {
            encodePassword(user);
        }

        return userRepository.save(user);
    }

    public void encodePassword(@NotNull User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    public boolean checkEmailUnique(Integer id, String email) {
        User userByEmail = userRepository.findByEmail(email);

        if (userByEmail == null) {
            return true;
        }

        boolean isCreatingNew = (id == null);

        if (isCreatingNew) {
            return false;
        } else {
            return Objects.equals(userByEmail.getId(), id);
        }
    }

    public User findById(Integer id) throws UserNotFoundException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UserNotFoundException(String.format("Could not find any user with ID %s", id));
        }
    }

    public void deleteById(Integer id) throws UserNotFoundException {
        if (checkUserExists(id)) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException(String.format("Could not find any user with ID %s", id));
        }
    }

    public void updateEnabledStatus(Integer id, boolean enabled) {
        userRepository.updateEnabledStatus(id, enabled);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateAccount(@NotNull User userInForm) {
        User userInDb = userRepository.findById(userInForm.getId()).get();

        if (!userInForm.getPassword().isEmpty()) {
            userInDb.setPassword(userInForm.getPassword());
            encodePassword(userInDb);
        }

        if (userInForm.getPhotos() != null) {
            userInDb.setPhotos(userInForm.getPhotos());
        }

        userInDb.setFirstName(userInForm.getFirstName());
        userInDb.setLastName(userInForm.getLastName());

        return userRepository.save(userInDb);
    }

}
