package dev.mvc.member;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;


public interface MemberDAOInter {
  /**
   * �ߺ� ���̵� �˻�
   * @param id
   * @return �ߺ� ���̵� ����
   */
  public int checkID(String id);
  
  /**
   * ȸ�� ����
   * @param memberVO
   * @return
   */
  public int create(MemberVO memberVO);
  
  /**
   * ȸ�� ��ü ���
   * @return
   */
  public List<MemberVO> list();
  
  /**
   * ȸ�� ��ȸ
   * @param memberno
   * @return
   */
  public MemberVO read(int memberno);
  
  /**
   * ȸ�� ���� ��ȸ
   * @param id
   * @return
   */
  public MemberVO readById(String id);
  
  /**
   * ���� ó��
   * @param memberVO
   * @return
   */
  public int update(MemberVO memberVO);
  
  /**
   * ȸ�� ���� ó��
   * @param memberno
   * @return
   */
  public int delete(int memberno);
  

  public int passwd_check(HashMap<Object, Object> map);

  public int passwd_update(HashMap<Object, Object> map);
  
  /**
   * �α��� 
   * @param map
   * @return
   */
  public int login(HashMap<String, Object> map);
  
  /**
   * �α��ε� ȸ�� �������� �˻��մϴ�.
   * @param session
   * @return true: ������
   */
  public boolean isMember(HttpSession session);
  
}