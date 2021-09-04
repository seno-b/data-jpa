package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnlyDto {

//    @Value("#{target.username + ' ' + target.age }")
    String getUsername();
}
