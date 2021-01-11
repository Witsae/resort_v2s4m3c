package dev.mvc.cate;

import java.util.List;

public interface CateProcInter {

  /**
   * <Xmp>
   * ī�װ� �׷� ���
   * <insert id="create" parameterType="CateVO"> 
   * </Xmp>
   * @param cateVO
   * @return ó���� ���ڵ� ����
   */
  public int create(CateVO cateVO);
  
  /**
   * ���
   * <xmp>
   * <select id="list_cateno_asc" resultType="CateVO">
   * </xmp> 
   * @return ���ڵ� ���
   */
  public List<CateVO> list_cateno_asc();
  
  /**
   * ���
   * <xmp>
   * <select id="list_seqno_asc" resultType="CateVO">
   * </xmp> 
   * @return ���ڵ� ���
   */
  public List<CateVO> list_seqno_asc();
  
  /**
   * ��ȸ
   * <xmp>
   *   <select id="read" resultType="CateVO" parameterType="int">
   * </xmp>  
   * @param cateno
   * @return
   */
  public CateVO read(int cateno);
  
  /**
   * ���� ó��
   * <xmp>
   *   <update id="update" parameterType="CateVO"> 
   * </xmp>
   * @param cateVO
   * @return ó���� ���ڵ� ����
   */
  public int update(CateVO cateVO);
  
  /**
   * ���� ó��
   * <xmp>
   *   <delete id="delete" parameterType="int">
   * </xmp> 
   * @param cateno
   * @return ó���� ���ڵ� ����
   */
  public int delete(int cateno);
  
  /**
   * ��� ���� ����
   * <xmp>
   * <update id="update_seqno_up" parameterType="int">
   * </xmp>
   * @param cateno
   * @return ó���� ���ڵ� ����
   */
  public int update_seqno_up(int cateno);

  /**
   * ��� ���� ����
   * <xmp>
   * <update id="update_seqno_down" parameterType="int">
   * </xmp>
   * @param cateno
   * @return ó���� ���ڵ� ����
   */
  public int update_seqno_down(int cateno);
  
  /**
   * 
   * @param cateVO
   * @return
   */
  public int update_visible(CateVO cateVO);
  
  /**
   * ī�װ���ȣ�� ���� ī�װ� ���
   * @param categrpno
   * @return
   */
  public List<CateVO> list_by_categrpno(int categrpno);
  
  /**
   * <xmp>
   *  ���� VO ��� join
   *  <select id="list_join" resultType="Categrp_Cate_join"> 
   * </xmp>
   * @return
   */
  public List<Categrp_Cate_join> list_join();  
  
  /**
   * <xmp>
   *  ���� VO ��� join
   *  <select id="list_join_by_categrpno" resultType="Categrp_Cate_join"> 
   * </xmp>
   * @return
   */
  public List<Categrp_Cate_join> list_join_by_categrpno(int categrpno); 
  
  /**
   * �� �� ����
   * @param cateno
   * @return
   */
  public int increaseCnt(int cateno);
  
  /**
   * �� �� ����
   * @param cateno
   * @return
   */
  public int decreaseCnt(int cateno);
  
}
