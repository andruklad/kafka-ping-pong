package com.colvir.kafkapingpong.repository;

import com.colvir.kafkapingpong.entity.MsgEvent;
import com.colvir.kafkapingpong.entity.MsgStatus;
import com.colvir.kafkapingpong.entity.MsgType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MsgEventRepository extends JpaRepository<MsgEvent, Integer> {

    List<MsgEvent> findAllByTypeAndStatus(MsgType type, MsgStatus status);
}
