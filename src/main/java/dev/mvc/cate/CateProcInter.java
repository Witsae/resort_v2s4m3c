package dev.mvc.cate;

import java.util.List;

public interface CateProcInter {

  /**
   * <Xmp>
   * 카테고리 그룹 등록
   * <insert id="create" parameterType="CateVO"> 
   * </Xmp>
   * @param cateVO
   * @return 처리된 레코드 갯수
   */
  public int create(CateVO cateVO);
  
  /**
   * 목록
   * <xmp>
   * <select id="list_cateno_asc" resultType="CateVO">
   * </xmp> 
   * @return 레코드 목록
   */
  public List<CateVO> list_cateno_asc();
  
  /**
   * 목록
   * <xmp>
   * <select id="list_seqno_asc" resultType="CateVO">
   * </xmp> 
   * @return 레코드 목록
   */
  public List<CateVO> list_seqno_asc();
  
  /**
   * 조회
   * <xmp>
   *   <select id="read" resultType="CateVO" parameterType="int">
   * </xmp>  
   * @param cateno
   * @return
   */
  public CateVO read(int cateno);
  
  /**
   * 수정 처리
   * <xmp>
   *   <update id="update" parameterType="CateVO"> 
   * </xmp>
   * @param cateVO
   * @return 처리된 레코드 갯수
   */
  public int update(CateVO cateVO);
  
  /**
   * 삭제 처리
   * <xmp>
   *   <delete id="delete" parameterType="int">
   * </xmp> 
   * @param cateno
   * @return 처리된 레코드 갯수
   */
  public int delete(int cateno);
  
  /**
   * 출력 순서 상향
   * <xmp>
   * <update id="update_seqno_up" parameterType="int">
   * </xmp>
   * @param cateno
   * @return 처리된 레코드 갯수
   */
  public int update_seqno_up(int cateno);

  /**
   * 출력 순서 하향
   * <xmp>
   * <update id="update_seqno_down" parameterType="int">
   * </xmp>
   * @param cateno
   * @return 처리된 레코드 갯수
   */
  public int update_seqno_down(int cateno);
  
  /**
   * 
   * @param cateVO
   * @return
   */
  public int update_visible(CateVO cateVO);
  
  /**
   * 카테고리번호에 속한 카테고리 목록
   * @param categrpno
   * @return
   */
  public List<CateVO> list_by_categrpno(int categrpno);
  
  /**
   * <xmp>
   *  통합 VO 기반 join
   *  <select id="list_join" resultType="Categrp_Cate_join"> 
   * </xmp>
   * @return
   */
  public List<Categrp_Cate_join> list_join();  
  
  /**
   * <xmp>
   *  통합 VO 기반 join
   *  <select id="list_join_by_categrpno" resultType="Categrp_Cate_join"> 
   * </xmp>
   * @return
   */
  public List<Categrp_Cate_join> list_join_by_categrpno(int categrpno); 
  
  /**
   * 글 수 증가
   * @param cateno
   * @return
   */
  public int increaseCnt(int cateno);
  
  /**
   * 글 수 감소
   * @param cateno
   * @return
   */
  public int decreaseCnt(int cateno);
  
}
