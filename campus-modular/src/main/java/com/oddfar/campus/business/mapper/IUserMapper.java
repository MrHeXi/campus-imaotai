package com.oddfar.campus.business.mapper;

import com.oddfar.campus.business.entity.IUser;
import com.oddfar.campus.common.core.BaseMapperX;
import com.oddfar.campus.common.core.LambdaQueryWrapperX;
import com.oddfar.campus.common.domain.PageResult;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * I茅台用户Mapper接口
 *
 * @author oddfar
 * @date 2023-07-02
 */
public interface IUserMapper extends BaseMapperX<IUser> {
    default PageResult<IUser> selectPage(IUser iUser) {

        return selectPage(new LambdaQueryWrapperX<IUser>()
                .eqIfPresent(IUser::getUserId, iUser.getUserId())
                .eqIfPresent(IUser::getMobile, iUser.getMobile())
                .eqIfPresent(IUser::getProvinceName, iUser.getProvinceName())
                .betweenIfPresent(IUser::getExpireTime, iUser.getParams())
                .orderByAsc(IUser::getCreateTime)
        );

    }

    default PageResult<IUser> selectPage(IUser iUser, Long userId) {

        return selectPage(new LambdaQueryWrapperX<IUser>()
                .eqIfPresent(IUser::getUserId, iUser.getUserId())
                .eqIfPresent(IUser::getMobile, iUser.getMobile())
                .eqIfPresent(IUser::getProvinceName, iUser.getProvinceName())
                .eq(IUser::getCreateUser, userId)
                .betweenIfPresent(IUser::getExpireTime, iUser.getParams())
                .orderByAsc(IUser::getCreateTime)
        );

    }

    default List<IUser> selectReservationUser() {
        return selectList(new LambdaQueryWrapperX<IUser>()
                .gt(IUser::getExpireTime, new Date())
                .ne(IUser::getLat, "")
                .ne(IUser::getLng, "")
                .ne(IUser::getShopType, "")
                .ne(IUser::getItemCode, "")

        );

    }

    /**
     * 通过预约执行分钟查询预约用户列表
     */
    default List<IUser> selectReservationUserByMinute(int minute) {
        return selectList(new LambdaQueryWrapperX<IUser>()
                .eq(IUser::getMinute, minute)
                .gt(IUser::getExpireTime, new Date())
                .ne(IUser::getLat, "")
                .ne(IUser::getLng, "")
                .ne(IUser::getShopType, "")
                .ne(IUser::getItemCode, "")
        );
    }

    @Select("UPDATE i_user SET `minute` = (SELECT FLOOR(RAND() * 59 + 1)) WHERE random_minute = \"0\"")
    void updateUserMinuteBatch();

    int deleteIUser(Long[] iUserId);
}
