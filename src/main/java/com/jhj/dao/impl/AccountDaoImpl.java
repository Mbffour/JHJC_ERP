package com.jhj.dao.impl;


import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by fangzhipeng on 2017/4/20.
 */
@Repository
public class AccountDaoImpl  {

//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//    @Override
//    public int add(Account account) {
//        return jdbcTemplate.update("insert into account(name, money) values(?, ?)",
//              account.getName(),account.getMoney());
//
//    }
//
//    @Override
//    public int update(Account account) {
//        return jdbcTemplate.update("UPDATE  account SET NAME=? ,money=? WHERE id=?",
//                account.getName(),account.getMoney(),account.getId());
//    }
//
//    @Override
//    public int delete(int id) {
//        return jdbcTemplate.update("DELETE from TABLE account where id=?",id);
//    }
//
//    @Override
//    public Account findAccountById(int id) {
//        List<Account> list = jdbcTemplate.query("select * from account where id = ?", new Object[]{id}, new BeanPropertyRowMapper(Account.class));
//        if(list!=null && list.size()>0){
//            Account account = list.get(0);
//            return account;
//        }else{
//            return null;
//        }
//    }
//
//    @Override
//    public List<Account> findAccountList() {
//        List<Account> list = jdbcTemplate.query("select * from account", new Object[]{}, new BeanPropertyRowMapper(Account.class));
//        if(list!=null && list.size()>0){
//            return list;
//        }else{
//            return null;
//        }
//    }
}
