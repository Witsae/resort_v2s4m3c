package dev.mvc.member;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;


public interface MemberDAOInter {
  /**
   * 중복 아이디 검사
   * @param id
   * @return 중복 아이디 갯수
   */
  public int checkID(String id);
  
  /**
   * 회원 가입
   * @param memberVO
   * @return
   */
  public int create(MemberVO memberVO);
  
  /**
   * 회원 전체 목록
   * @return
   */
  public List<MemberVO> list();
  
  /**
   * 회원 조회
   * @param memberno
   * @return
   */
  public MemberVO read(int memberno);
  
  /**
   * 회원 정보 조회
   * @param id
   * @return
   */
  public MemberVO readById(String id);
  
  /**
   * 수정 처리
   * @param memberVO
   * @return
   */
  public int update(MemberVO memberVO);
  
  /**
   * 회원 삭제 처리
   * @param memberno
   * @return
   */
  public int delete(int memberno);
  

  public int passwd_check(HashMap<Object, Object> map);

  public int passwd_update(HashMap<Object, Object> map);
  
  /**
   * 로그인 
   * @param map
   * @return
   */
  public int login(HashMap<String, Object> map);
  
  /**
   * 로그인된 회원 계정인지 검사합니다.
   * @param session
   * @return true: 관리자
   */
  public boolean isMember(HttpSession session);
  
}