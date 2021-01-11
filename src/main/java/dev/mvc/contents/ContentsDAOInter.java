package dev.mvc.contents;

import java.util.HashMap;
import java.util.List;


public interface ContentsDAOInter {
  
  /**
   * <Xmp>
   * ���
   * <insert id="create" parameterType="ContentsVO"> 
   * </Xmp>
   * @param contentsVO
   * @return ó���� ���ڵ� ����
   */
  public int create(ContentsVO contentsVO);
  
  
  /**
   * �Խ��� �� ��ü ����Դϴ�.
   * @return
   */
  public List<ContentsVO> list_all();
  
  /**
   * �ش� ī�װ��� ���� ������ ���
   * @return
   */
  public List<ContentsVO> list_by_cateno(int cateno);
  
  /**
   * ��ȸ
   * <xmp>
   *   <select id="read" resultType="ContentsVO" parameterType="int">
   * </xmp>  
   * @param contentsno
   * @return
   */
  public ContentsVO read(int contentsno);
  
  /**
   * ���� ó��
   * <xmp>
   *   <update id="update" parameterType="ContentsVO"> 
   * </xmp>
   * @param contentsVO
   * @return ó���� ���ڵ� ����
   */
  public int update(ContentsVO contentsVO);
  
  /**
   * �н����� �˻�
   * @param hashMap
   * @return
   */
  public int passwd_check(HashMap hashMap);
  
  /**
   * ����
   * @param contentsno
   * @return cnt
   */
  public int delete(int contentsno);
  
  /**
   * �̹������� ó��
   * <xmp>
   *   <update id="update_img" parameterType="ContentsVO"> 
   * </xmp>
   * @param contentsVO
   * @return ó���� ���ڵ� ����
   */
  public int update_img(ContentsVO contentsVO);
  
  /**
   * ��ü ���ڵ� ����
   * @return
   */
  public int total_count();
 
 
  /**
   * Map ���, ����, ����
   * @param hashmap
   * @return
   */
  public int map(HashMap hashmap);
  
  /**
   * Map ��ȸ
   * @param contentsno
   * @return
   */
  public ContentsVO map_read(int contentsno);
  
  /**
   * ��Ʃ�� ���, ����, ����
   * @param hashmap
   * @return
   */
  public int youtube(HashMap hashmap);
  
  /**
   * mp3 ���, ����, ����
   * @param hashmap
   * @return
   */
  public int mp3(HashMap hashmap);
  
  /**
   * mp4 ���, ����, ����
   * @param hashmap
   * @return
   */
  public int mp4(HashMap hashmap);
  
  /**
   * ī�װ��� �˻� ���
   * @param hashMap
   * @return
   */
  public List<ContentsVO> list_by_cateno_search(HashMap<String, Object> hashMap);

  /**
   * ī�װ��� �˻� ���ڵ� ����
   * @param hashMap
   * @return
   */
  public int search_count(HashMap<String, Object> hashMap);
  
  /**
   * <xmp>
   * �˻� + ����¡ ���
   * <select id="list_by_cateno_search_paging" resultType="ContentsVO" parameterType="HashMap">
   * </xmp>
   * @param map
   * @return
   */
  public List<ContentsVO> list_by_cateno_search_paging(HashMap<String, Object> map);
  
  /**
   * �亯 ���� ����
   * <update id="increaseAnsnum" parameterType="HashMap"> 
   * @param map
   * @return
   */
  public int increaseAnsnum(HashMap<String, Object> map);
   
  /**
   * <xmp>
   * �亯
   * <insert id="reply" parameterType="ContentsVO">
   * </xmp>
   * @param contentsVO
   * @return
   */
  public int reply(ContentsVO contentsVO);
  
  /**
   * <xmp>
   * �˻� + ����¡ ���
   * <select id="list_by_cateno_search_paging" resultType="ContentsVO" parameterType="HashMap">
   * </xmp>
   * @param map
   * @return
   */
  public List<Contents_MemberVO> list_by_cateno_search_paging_join(HashMap<String, Object> map);
  
  /**
   * �� �� ����
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int contentsno);
 
  /**
   * �� �� ����
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int contentsno);
  
  
}
