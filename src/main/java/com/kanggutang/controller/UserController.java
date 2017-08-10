package com.kanggutang.controller;

import com.kanggutang.common.*;
import com.kanggutang.dto.BaseSubCategoriesDataInfoDTO;
import com.kanggutang.dto.UserDTO;
import com.kanggutang.model.User;
import com.kanggutang.response.BaseResponse;
import com.kanggutang.response.LoginResponse;
import com.kanggutang.service.BaseSubCategoriesDataInfoService;
import com.kanggutang.service.PermissionService;
import com.kanggutang.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liaoyubo
 * @version 1.0 2017/5/11
 * @description
 */
@Controller
public class UserController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private BaseSubCategoriesDataInfoService baseSubCategoriesDataInfoService;

    @Value("${user.sessionInvalid}")
    private String userSessionInvalid;

    @RequestMapping(value = "/loginPage",method = RequestMethod.GET)
    public void loginPage(){

    }
    @RequestMapping(value = "/addUserPage",method = RequestMethod.GET)
    public void addUserPage(Map<String,Object> map){
        BaseSubCategoriesDataInfoDTO baseSubCategoriesDataInfoDTO = new BaseSubCategoriesDataInfoDTO();
        baseSubCategoriesDataInfoDTO.setCategoriesName("ROLE_TYPE");
        List<BaseSubCategoriesDataInfoDTO> baseSubCategoriesDataInfoDTOList = baseSubCategoriesDataInfoService.getCodeAndValueByBatch(baseSubCategoriesDataInfoDTO);
        map.put("roleTypeList",baseSubCategoriesDataInfoDTOList);
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public LoginResponse login(@RequestBody UserDTO userDTO){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            String loginName = userDTO.getLoginName();
            String password = userDTO.getPassword();
            if(loginName == null || "".equals(loginName)
                    || password == null || "".equals(password)){
                loginResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                loginResponse.setResponseDesc("用户名或者密码不能为空");
                return loginResponse;
            }
            userDTO.setPassword("");
            userDTO = userService.getUser(userDTO);
            if(userDTO == null){
                loginResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                loginResponse.setResponseDesc("用户名或者密码不正确");
                return loginResponse;
            }
            if (!PasswordHash.validatePassword(password, userDTO.getPassword())) {
                loginResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                loginResponse.setResponseDesc("用户名或者密码不正确");
                return loginResponse;
            }
            loginResponse.setUserId(userDTO.getUserId());
            loginResponse.setLoginName(userDTO.getLoginName());
            String accessToken = ServiceUtil.CreateAccessToken(loginName, String.valueOf(System.currentTimeMillis()));
            loginResponse.setAccessToken(accessToken);
            //实现权限
            List<String> list = permissionService.getFunctionList(userDTO.getRoleType());
            Long invalid = Long.parseLong(userSessionInvalid) * 60;
            redisUtil.set(accessToken, list, invalid);
            loginResponse.setResponseDesc("登陆成功");
        }catch (Exception e){
            logger.error("登陆失败:"+e);
            loginResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            loginResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return loginResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/loginOut",method = RequestMethod.POST)
    public BaseResponse loginOut(@RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        String loginName = userDTO.getLoginName();
        String accessToken = userDTO.getAccessToken();
        if(loginName == null || "".equals(loginName)){
            baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
            baseResponse.setResponseDesc("账号不能为空");
            return baseResponse;
        }
        if(accessToken == null || "".equals(accessToken)){
            baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
            baseResponse.setResponseDesc("授权码不能为空");
            return baseResponse;
        }
        redisUtil.remove(accessToken);
        baseResponse.setResponseDesc("用户退出系统成功");
        return baseResponse;
    }

    @ResponseBody
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public BaseResponse addUser(@RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            String loginName = userDTO.getLoginName();
            String password = userDTO.getPassword();
            Integer roleType = userDTO.getRoleType();
            String phone = userDTO.getPhone();
            if(loginName == null || "".equals(loginName)
                    || password == null || "".equals(password)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("用户名不能为空");
                return baseResponse;
            }
            if(password == null || "".equals(password)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("密码不能为空");
                return baseResponse;
            }
            if(roleType == null || roleType == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("用户类型不能为空");
                return baseResponse;
            }
            if(phone == null || "".equals(phone)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("电话不能为空");
                return baseResponse;
            }
            userDTO.setPassword("");
            userDTO = userService.getUser(userDTO);
            if(userDTO == null){
                User user = new User();
                user.setUserName(loginName);
                user.setLoginName(loginName);
                user.setPassword(PasswordHash.createHash(password));
                user.setRoleType(roleType);
                user.setPhone(phone);
                user.setStatus(1);
                user.setCreationDate(new Date());
                user.setCreatedBy(-1);
                user.setLastUpdateDate(new Date());
                user.setLastUpdatedBy(-1);
                userService.addUser(user);
                baseResponse.setResponseDesc("新增用户成功");
            }else {
                //判断是否是管理员
                if(userDTO.getRoleType() == roleType){
                    baseResponse.setResponseDesc("管理员已经存在，不能再新增管理员");
                }else {
                    baseResponse.setResponseDesc("用户名已经存在");
                }
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                return baseResponse;
            }
        }catch (Exception e){
            logger.error("新增用户失败:"+e);
            baseResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            baseResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return baseResponse;
    }

    @RequestMapping(value = "/mainPage")
    public void mainPage(){

    }

    @RequestMapping(value = "/updatePwdPage")
    public void updatePwdPage(){

    }

    @RequestMapping(value = "/updatePwd",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updatePwd(@RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            Integer userId = userDTO.getUserId();
            String password = userDTO.getPassword();
            String newPassword = userDTO.getNewPassword();
            String confirmPassword = userDTO.getConfirmPassword();
            if(userId == null || userId == 0){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("当前用户id不能为空");
                return baseResponse;
            }
            if(password == null || "".equals(password)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("原密码不能为空");
                return baseResponse;
            }
            if(newPassword == null || "".equals(newPassword)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("新密码不能为空");
                return baseResponse;
            }
            if(confirmPassword == null || "".equals(confirmPassword)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("确认密码不能为空");
                return baseResponse;
            }
            if(!newPassword.equals(confirmPassword)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("新密码和确认密码不相同");
                return baseResponse;
            }
            UserDTO user = new UserDTO();
            user.setUserId(userDTO.getUserId());
            user = userService.getUser(user);
            if(!PasswordHash.validatePassword(password, user.getPassword())){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("用户原始密码不对");
                return baseResponse ;
            }
            userDTO.setNewPassword(PasswordHash.createHash(newPassword));
            userService.updatePwd(userDTO);
            baseResponse.setResponseDesc("修改密码成功");
        }catch (Exception e){
            logger.error("修改密码出错:"+e);
            baseResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            baseResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return baseResponse;
    }

    @RequestMapping(value = "/resetPwdPage")
    public void resetPwdPage(){

    }

    @RequestMapping(value = "/resetPwd",method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse resetPwd(@RequestBody UserDTO userDTO){
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setResponseCode(ResponseCode.SUCCESS);
        try {
            String loginName = userDTO.getLoginName();
            String phone = userDTO.getPhone();
            if(loginName == null || "".equals(loginName)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("账号不能为空");
                return baseResponse;
            }
            if(phone == null || "".equals(phone)){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("电话不能为空");
                return baseResponse;
            }
            userDTO = userService.getUser(userDTO);
            if(userDTO == null){
                baseResponse.setResponseCode(ResponseCode.PARAMETER_ERROR);
                baseResponse.setResponseDesc("账号不存在或账号与电话不一致");
                return baseResponse;
            }
            String randPwd = RandCodeUtil.getRandomPwd(6);
            userDTO.setNewPassword(PasswordHash.createHash(randPwd));
            userService.updatePwd(userDTO);
            baseResponse.setResponseDesc("重置密码成功,重置后的密码为:"+randPwd);
        }catch (Exception e){
            logger.error("重置密码错误:"+e);
            baseResponse.setResponseCode(ResponseCode.SERVER_ERROR);
            baseResponse.setResponseDesc("服务器错误");
            e.printStackTrace();
        }
        return baseResponse;
    }
}
