/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mtons.mblog.modules.service.impl;

import com.mtons.mblog.base.consts.EntityStatus;
import com.mtons.mblog.service.exception.MtonsException;
import com.mtons.mblog.service.comp.IPasswdService;
import com.mtons.mblog.service.comp.IUserPassportService;
import com.mtons.mblog.bo.AccountProfile;
import com.mtons.mblog.bo.BadgesCount;
import com.mtons.mblog.bo.UserBO;
import com.mtons.mblog.entity.User;
import com.mtons.mblog.dao.repository.RoleRepository;
import com.mtons.mblog.dao.repository.UserRepository;
import com.mtons.mblog.service.atom.impl.BaseService;
import com.mtons.mblog.service.seq.SeqType;
import com.mtons.mblog.service.seq.container.ISeqContainer;
import com.mtons.mblog.modules.service.MessageService;
import com.mtons.mblog.service.atom.UserService;
import com.mtons.mblog.base.utils.BeanMapUtils;
import com.yueny.rapid.lang.exception.invalid.InvalidException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 用户服务，该服务提供的对象，密码均为空
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseService implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private IUserPassportService userPassportService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IPasswdService passwdService;
    @Autowired
    private ISeqContainer seqContainer;

    @Override
    public Page<UserBO> paging(Pageable pageable, String name) {
        Page<User> page = userRepository.findAll((root, query, builder) -> {
            Predicate predicate = builder.conjunction();

            if (StringUtils.isNoneBlank(name)) {
                predicate.getExpressions().add(
                        builder.like(root.get("name"), "%" + name + "%"));
            }

            query.orderBy(builder.desc(root.get("id")));
            return predicate;
        }, pageable);

        List<UserBO> rets = new ArrayList<>();
        page.getContent().forEach(n -> {
            UserBO userBO = map(n, UserBO.class);
            userBO.setPassword("");
            rets.add(userBO);
        });
        return new PageImpl<>(rets, pageable, page.getTotalElements());
    }

    @Override
    public Map<Long, UserBO> findMapByIds(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyMap();
        }

        List<User> list = userRepository.findAllById(ids);
        Map<Long, UserBO> ret = new HashMap<>();

        list.forEach(po -> {
            UserBO userBO = map(po, UserBO.class);
            userBO.setPassword("");
            ret.put(po.getId(), userBO);
        });

        return ret;
    }

    @Override
    @Transactional
    public AccountProfile login(String username, String password) {
        User po = userRepository.findByUsername(username);
        Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");

        Assert.state(StringUtils.equals(po.getPassword(), password), "密码错误");

        po.setLastLogin(Calendar.getInstance().getTime());
        userRepository.save(po);

        AccountProfile u = mapAny(po, AccountProfile.class);
        //AccountProfile u = BeanMapUtils.copyPassport(po);

        BadgesCount badgesCount = new BadgesCount();
        badgesCount.setMessages(messageService.unread4Me(u.getId()));

        u.setBadgesCount(badgesCount);
        return u;
    }

    @Override
    @Transactional
    public AccountProfile findProfile(Long id) {
        User po = userRepository.findById(id).get();

        Assert.notNull(po, "账户不存在");

//		Assert.state(po.getStatus() != Const.STATUS_CLOSED, "您的账户已被封禁");
        po.setLastLogin(Calendar.getInstance().getTime());

        AccountProfile accountProfile = mapAny(po, AccountProfile.class);

        BadgesCount badgesCount = new BadgesCount();
        badgesCount.setMessages(messageService.unread4Me(accountProfile.getId()));

        accountProfile.setBadgesCount(badgesCount);

        return accountProfile;
    }

    @Override
    @Transactional
    public UserBO register(UserBO user) {
        Assert.notNull(user, "Parameter user can not be null!");

        Assert.hasLength(user.getUsername(), "用户名不能为空!");
        Assert.hasLength(user.getPassword(), "密码不能为空!");

        User check = userRepository.findByUsername(user.getUsername());

        Assert.isNull(check, "用户名已经存在!");

        User po = map(user, User.class);

        if (StringUtils.isBlank(po.getName())) {
            po.setName(user.getUsername());
        }

        Date now = Calendar.getInstance().getTime();

        // 密码加密方式
        String pw = passwdService.encode(user.getPassword(), "");
        po.setPassword(pw);
        po.setStatus(EntityStatus.ENABLED);
        po.setCreated(now);

        String uid = seqContainer.getStrategy(SeqType.USER_U_ID).get("");
        po.setUid(uid);
        // 默认
        po.setDomainHack(seqContainer.getStrategy(SeqType.SIMPLE).get(""));

        userRepository.save(po);

        return map(po, UserBO.class);
    }

    @Override
    @Transactional
    public AccountProfile update(UserBO user) {
        User po = userRepository.findById(user.getId()).get();
        po.setName(user.getName());
        po.setSignature(user.getSignature());

        if(StringUtils.isNotEmpty(user.getDomainHack())){
            po.setDomainHack(user.getDomainHack());
        }else{
            po.setDomainHack(String.valueOf(user.getId()));
        }

        userRepository.save(po);

        // BeanMapUtils.copyPassport(po);
        return mapAny(po, AccountProfile.class);
    }

    @Override
    @Transactional
    public AccountProfile updateEmail(long id, String email) {
        User po = userRepository.findById(id).get();

        if (email.equals(po.getEmail())) {
            throw new MtonsException("邮箱地址没做更改");
        }

        User check = userRepository.findByEmail(email);

        if (check != null && check.getId() != po.getId()) {
            throw new MtonsException("该邮箱地址已经被使用了");
        }
        po.setEmail(email);
        userRepository.save(po);

        return mapAny(po, AccountProfile.class);
    }

    @Override
    public UserBO get(long userId) {
        Optional<User> optional = userRepository.findById(userId);
        if (optional.isPresent()) {
            User user = optional.get();
            if (user == null) {
                return null;
            }

            return map(user, UserBO.class);
        }
        return null;
    }

    @Override
    public UserBO getByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }

        return map(user, UserBO.class);
    }

    @Override
    public UserBO getByEmail(String email) {
        return BeanMapUtils.copy(userRepository.findByEmail(email));
    }

    @Override
    @Transactional
    public AccountProfile updateAvatar(long id, String path) {
        User po = userRepository.findById(id).get();
        po.setAvatar(path);
        userRepository.save(po);

        return mapAny(po, AccountProfile.class);
    }

    @Override
    @Transactional
    public boolean updatePassword(String uid, String newPassword) throws InvalidException{
        Assert.hasLength(newPassword, "密码不能为空!");

        return passwdService.changePassword(uid, newPassword);
    }

    @Override
    @Transactional
    public boolean updatePassword(String uid, String oldPassword, String newPassword) throws InvalidException {
        Assert.hasLength(newPassword, "新密码不能为空!");

        return userPassportService.modifyPassPort(uid, oldPassword, newPassword);

//        String pw = passwdService.encode(newPassword, "");
//        User po = userRepository.findByUid(uid);
//        po.setPassword(pw);
//        userRepository.save(po);
    }

    @Override
    @Transactional
    public boolean updateStatus(long id, int status) {
        User po = userRepository.findById(id).get();

        po.setStatus(status);
        return userRepository.updateStatus(id, status) == 1;
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public UserBO getByDomainHack(String domainHack) {
        User user = userRepository.findByDomainHack(domainHack);
        if (user == null) {
            return null;
        }

        return map(user, UserBO.class);
//        return BeanMapUtils.copy(userRepository.findByDomainHack(domainHack));
    }

}
