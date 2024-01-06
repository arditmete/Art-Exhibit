package com.edu.artexhibit.service;


import java.util.List;

public interface IEntityService <S> {
     S findById(long id);
     List<S> findAll(int page, int size);
     S findByName(String name);
     S update(S S, long id );
     String delete(long id);
     S create(S S);
     boolean checkIfExists(S s);
}
