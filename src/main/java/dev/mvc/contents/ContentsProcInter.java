package dev.mvc.contents;

import java.util.HashMap;
import java.util.List;

public interface ContentsProcInter {

  /**
   * <Xmp>
   * 등록
   * <insert id="create" parameterType="ContentsVO"> 
   * </Xmp>
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int create(ContentsVO contentsVO);
  
  /**
   * 게시판 글 전체 목록입니다.
   * @return
   */
  public List<ContentsVO> list_all();
  
  /**
   * 해당 카테고리에 속한 콘텐츠 목록
   * @return
   */
  public List<ContentsVO> list_by_cateno(int cateno);
  
  /**
   * 조회
   * <xmp>
   *   <select id="read" resultType="ContentsVO" parameterType="int">
   * </xmp>  
   * @param contentsno
   * @return
   */
  public ContentsVO read(int contentsno);
  
  /**
   * 수정용 조회
   * @param contentsno
   * @return
   */
  public ContentsVO read_update(int contentsno);
  
  /**
   * 수정 처리
   * <xmp>
   *   <update id="update" parameterType="ContentsVO"> 
   * </xmp>
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int update(ContentsVO contentsVO);
  
  /**
   * 패스워드 검사
   * @param hashMap
   * @return
   */
  public int passwd_check(HashMap hashMap);
  
  /**
   * 삭제
   * @param contentsno
   * @return cnt
   */
  public int delete(int contentsno);
  
  /**
   * 이미지추가 처리
   * <xmp>
   *   <update id="update_img" parameterType="ContentsVO"> 
   * </xmp>
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int img_create(ContentsVO contentsVO);

  /**
   * 이미지수정 처리
   * <xmp>
   *   <update id="update_img" parameterType="ContentsVO"> 
   * </xmp>
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int img_update(ContentsVO contentsVO);
  
  /**
   * 이미지삭제 처리
   * <xmp>
   *   <update id="update_img" parameterType="ContentsVO"> 
   * </xmp>
   * @param contentsVO
   * @return 처리된 레코드 갯수
   */
  public int img_delete(ContentsVO contentsVO);
  
  /**
   * 전체 레코드 갯수
   * @return
   */
  public int total_count();
  
  /**
   * Map 등록, 변경, 삭제
   * @param hashmap
   * @return
   */
  public int map(HashMap hashmap);
  
  /**
   * Map 조회
   * @param contentsno
   * @return
   */
  public ContentsVO map_read(int contentsno);
  
  /**
   * 유튜브 등록, 변경, 삭제
   * @param hashmap
   * @return
   */
  public int youtube(HashMap hashmap);
  
  /**
   * mp3 등록, 변경, 삭제
   * @param hashmap
   * @return
   */
  public int mp3(HashMap hashmap);
  
  /**
   * mp4 등록, 변경, 삭제
   * @param hashmap
   * @return
   */
  public int mp4(HashMap hashmap);
  
  /**
   * 카테고리별 검색 목록
   * @param hashMap
   * @return
   */
  public List<ContentsVO> list_by_cateno_search(HashMap<String, Object> hashMap);

  /**
   * 카테고리별 검색 레코드 갯수
   * @param hashMap
   * @return
   */
  public int search_count(HashMap<String, Object> hashMap);
  
  /**
   * 페이지 목록 문자열 생성, Box 형태
   * @param listFile 목록 파일명 
   * @param categrpno 카테고리번호
   * @param search_count 검색 갯수
   * @param nowPage 현재 페이지, nowPage는 1부터 시작
   * @param word 검색어
   * @return
   */
  public String pagingBox(String listFile, int categrpno, int search_count, int nowPage, String word);
  
  /**
   * <xmp>
   * 검색 + 페이징 목록
   * <select id="list_by_cateno_search_paging" resultType="ContentsVO" parameterType="HashMap">
   * </xmp>
   * @param map
   * @return
   */
  public List<ContentsVO> list_by_cateno_search_paging(HashMap<String, Object> map);
  
  /**
   * 답변 순서 증가
   * <update id="increaseAnsnum" parameterType="HashMap"> 
   * @param map
   * @return
   */
  public int increaseAnsnum(HashMap<String, Object> map);
   
  /**
   * <xmp>
   * 답변
   * <insert id="reply" parameterType="ContentsVO">
   * </xmp>
   * @param contentsVO
   * @return
   */
  public int reply(ContentsVO contentsVO);
  
  /**
   * <xmp>
   * 검색 + 페이징 목록
   * <select id="list_by_cateno_search_paging" resultType="ContentsVO" parameterType="HashMap">
   * </xmp>
   * @param map
   * @return
   */
  public List<Contents_MemberVO> list_by_cateno_search_paging_join(HashMap<String, Object> map);
  
  /**
   * 글 수 증가
   * @param 
   * @return
   */ 
  public int increaseReplycnt(int contentsno);
 
  /**
   * 글 수 감소
   * @param 
   * @return
   */   
  public int decreaseReplycnt(int contentsno);
  
}
