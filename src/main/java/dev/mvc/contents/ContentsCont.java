package dev.mvc.contents;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import dev.mvc.attachfile.AttachfileProcInter;
import dev.mvc.attachfile.AttachfileVO;
import dev.mvc.cate.CateProcInter;
import dev.mvc.cate.CateVO;
import dev.mvc.categrp.CategrpProcInter;
import dev.mvc.categrp.CategrpVO;
import dev.mvc.member.MemberProcInter;
import dev.mvc.tool.Tool;
import dev.mvc.tool.Upload;

@Controller
public class ContentsCont {
  @Autowired
  @Qualifier("dev.mvc.member.MemberProc")
  private MemberProcInter memberProc = null;

  @Autowired
  @Qualifier("dev.mvc.categrp.CategrpProc")
  private CategrpProcInter categrpProc;

  @Autowired
  @Qualifier("dev.mvc.cate.CateProc")
  private CateProcInter cateProc;

  @Autowired
  @Qualifier("dev.mvc.contents.ContentsProc")
  private ContentsProcInter contentsProc;

  @Autowired
  @Qualifier("dev.mvc.attachfile.AttachfileProc")
  private AttachfileProcInter attachfileProc;

  public ContentsCont() {
    System.out.println("--> ContentsCont created.");
  }

  /**
   * ����� http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/create.do", method = RequestMethod.GET)
  public ModelAndView create(int cateno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    mav.setViewName("/contents/create");

    return mav;
  }

  /**
   * ��� ó�� http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  /*
   * @RequestMapping(value="/contents/create.do", method=RequestMethod.POST)
   * public ModelAndView create(HttpServletRequest request, ContentsVO contentsVO)
   * { //System.out.println("IP: " + request.getRemoteAddr()); //0:0:0:0:0:0:0:1 ��
   * �ڱ��ڽ��� ��Ÿ�� contentsVO.setIp(request.getRemoteAddr());
   * 
   * ModelAndView mav = new ModelAndView();
   * 
   * 
   * // ------------------------------------------------------------------- // ����
   * ���� �ڵ� ���� //
   * ------------------------------------------------------------------- String
   * file1 = ""; // main image String thumb1 = ""; // preview image
   * 
   * String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); //
   * ���� ��� // ���� ������ ����� fnamesMF ��ü�� ������. // <input type='file'
   * class="form-control" name='file1MF' id='file1MF' // value=''
   * placeholder="���� ����" multiple="multiple"> MultipartFile mf =
   * contentsVO.getFile1MF(); long size1 = mf.getSize(); // ���� ũ�� if (size1 > 0) {
   * // ���� ũ�� üũ // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg // ���� ����
   * �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg... file1 =
   * Upload.saveFileSpring(mf, upDir);
   * 
   * if (Tool.isImage(file1)) { // �̹������� �˻� // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 200,
   * height: 150 thumb1 = Tool.preview(upDir, file1, 200, 150); }
   * 
   * }
   * 
   * contentsVO.setFile1(file1); contentsVO.setThumb1(thumb1);
   * contentsVO.setSize1(size1); //
   * ------------------------------------------------------------------- // ���� ����
   * �ڵ� ���� // -------------------------------------------------------------------
   * 
   * 
   * int cnt = this.contentsProc.create(contentsVO); mav.addObject("cnt", cnt); if
   * (cnt == 1) { cateProc.increaseCnt(contentsVO.getCateno()); }
   * mav.addObject("url", "create_msg"); mav.addObject("cateno",
   * contentsVO.getCateno()); mav.setViewName("redirect:/contents/msg.do");
   * 
   * return mav; // foward �� <c:import> �� �۵��� �ȵ� }
   */

  /**
   * ��� ó�� http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/create.do", method = RequestMethod.POST)
  public ModelAndView create(HttpServletRequest request, ContentsVO contentsVO) {
    // System.out.println("IP: " + request.getRemoteAddr()); //0:0:0:0:0:0:0:1 ��
    // �ڱ��ڽ��� ��Ÿ��
    contentsVO.setIp(request.getRemoteAddr());

    ModelAndView mav = new ModelAndView();

    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------
    String file1 = ""; // main image
    String thumb1 = ""; // preview image

    String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
    // ���� ������ ����� fnamesMF ��ü�� ������.
    // <input type='file' class="form-control" name='file1MF' id='file1MF'
    // value='' placeholder="���� ����" multiple="multiple">
    MultipartFile mf = contentsVO.getFile1MF();
    long size1 = mf.getSize(); // ���� ũ��
    if (size1 > 0) { // ���� ũ�� üũ
      // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
      // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
      file1 = Upload.saveFileSpring(mf, upDir);

      if (Tool.isImage(file1)) { // �̹������� �˻�
        // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 200, height: 150
        thumb1 = Tool.preview(upDir, file1, 200, 150);
      }

    }

    contentsVO.setFile1(file1);
    contentsVO.setThumb1(thumb1);
    contentsVO.setSize1(size1);
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------

    int cnt = this.contentsProc.create(contentsVO);
    // -------------------------------------------------------------------
    // PK�� return
    // -------------------------------------------------------------------
    System.out.println("--> " + contentsVO.getContentsno());
    mav.addObject("contentsno", contentsVO.getContentsno());
    // -------------------------------------------------------------------
    mav.addObject("cnt", cnt);
    if (cnt == 1) {
      cateProc.increaseCnt(contentsVO.getCateno());
    }
    mav.addObject("url", "create_continue");
    mav.addObject("cateno", contentsVO.getCateno());
    mav.setViewName("redirect:/contents/msg.do");

    return mav; // foward �� <c:import> �� �۵��� �ȵ�
  }

  /**
   * ���ΰ�ħ�� �����ϴ� �޽��� ���
   * 
   * @param contentsno
   * @return
   */
  @RequestMapping(value = "/contents/msg.do", method = RequestMethod.GET)
  public ModelAndView msg(String url) {
    ModelAndView mav = new ModelAndView();

    // ��� ó�� �޽���: create_msg --> /contents/create_msg.jsp
    // ���� ó�� �޽���: update_msg --> /contents/update_msg.jsp
    // ���� ó�� �޽���: delete_msg --> /contents/delete_msg.jsp
    mav.setViewName("/contents/" + url); // forward

    return mav; // forward
  }

  /**
   * ��� http://localhost:9090/resort/contents/list_all.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/list_all.do", method = RequestMethod.GET)
  public ModelAndView list() {

    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/list_all"); // /webapp/contents/list_all.jsp

    List<ContentsVO> list = this.contentsProc.list_all();
    mav.addObject("list", list);

    return mav;
  }

  /**
   * ī�װ� �� ��� http://localhost:9090/resort/contents/list_by_cateno.do
   * 
   * @return
   */
  /*
   * @RequestMapping(value="/contents/list.do", method=RequestMethod.GET) public
   * ModelAndView list_by_cateno(int cateno) {
   * 
   * ModelAndView mav = new ModelAndView();
   * mav.setViewName("/contents/list_by_cateno_table_img1"); //
   * /webapp/contents/list_by_cateno.jsp
   * 
   * CateVO cateVO = this.cateProc.read(cateno); mav.addObject("cateVO", cateVO);
   * 
   * CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
   * mav.addObject("categrpVO", categrpVO);
   * 
   * List<ContentsVO> list = this.contentsProc.list_by_cateno(cateno);
   * mav.addObject("list", list);
   * 
   * return mav; }
   */

  /**
   * ��� + �˻� ���� http://localhost:9090/resort/contents/list.do
   * http://localhost:9090/resort/contents/list.do?cateno=1&word=������
   * 
   * @param cateno
   * @param word
   * @return
   */
  /*
   * @RequestMapping(value = "/contents/list.do", method = RequestMethod.GET)
   * public ModelAndView list_by_cateno_search(
   * 
   * @RequestParam(value="cateno", defaultValue="1") int cateno,
   * 
   * @RequestParam(value="word", defaultValue="") String word ) {
   * 
   * ModelAndView mav = new ModelAndView(); // /contents/list_by_cateno_search.jsp
   * mav.setViewName("/contents/list_by_cateno_table_img1_search");
   * 
   * // ���ڿ� ���ڿ� Ÿ���� �����ؾ������� Obejct ��� HashMap<String, Object> map = new
   * HashMap<String, Object>(); map.put("cateno", cateno); // #{cateno}
   * map.put("word", word); // #{word}
   * 
   * // �˻� ��� List<ContentsVO> list = contentsProc.list_by_cateno_search(map);
   * mav.addObject("list", list);
   * 
   * // �˻��� ���ڵ� ���� int search_count = contentsProc.search_count(map);
   * mav.addObject("search_count", search_count);
   * 
   * CateVO cateVO = cateProc.read(cateno); mav.addObject("cateVO", cateVO);
   * 
   * CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
   * mav.addObject("categrpVO", categrpVO);
   * 
   * return mav; }
   */

  /**
   * ��� + �˻� + ����¡ ���� http://localhost:9090/resort/contents/list.do
   * http://localhost:9090/resort/contents/list.do?cateno=1&word=������&nowPage=1
   * 
   * @param cateno
   * @param word
   * @param nowPage
   * @return
   *//*
      * @RequestMapping(value = "/contents/list.do", method = RequestMethod.GET)
      * public ModelAndView list_by_cateno_search_paging(
      * 
      * @RequestParam(value="cateno", defaultValue="1") int cateno,
      * 
      * @RequestParam(value="word", defaultValue="") String word,
      * 
      * @RequestParam(value="nowPage", defaultValue="1") int nowPage ) {
      * System.out.println("--> nowPage: " + nowPage);
      * 
      * ModelAndView mav = new ModelAndView();
      * 
      * // ���ڿ� ���ڿ� Ÿ���� �����ؾ������� Obejct ��� HashMap<String, Object> map = new
      * HashMap<String, Object>(); map.put("cateno", cateno); // #{cateno}
      * map.put("word", word); // #{word} map.put("nowPage", nowPage); // �������� �����
      * ���ڵ��� ������ �����ϱ����� ���
      * 
      * // �˻� ��� List<ContentsVO> list =
      * contentsProc.list_by_cateno_search_paging(map); mav.addObject("list", list);
      * 
      * // �˻��� ���ڵ� ���� int search_count = contentsProc.search_count(map);
      * mav.addObject("search_count", search_count);
      * 
      * CateVO cateVO = cateProc.read(cateno); mav.addObject("cateVO", cateVO);
      * 
      * CategrpVO categrpVO = categrpProc.read(cateVO.getCategrpno());
      * mav.addObject("categrpVO", categrpVO);
      * 
      * 
      * SPAN�±׸� �̿��� �ڽ� ���� ����, 1 ���������� ���� ���� ������: 11 / 22 [����] 11 12 13 14 15 16 17
      * 18 19 20 [����]
      * 
      * @param listFile ��� ���ϸ�
      * 
      * @param cateno ī�װ���ȣ
      * 
      * @param search_count �˻�(��ü) ���ڵ��
      * 
      * @param nowPage ���� ������
      * 
      * @param word �˻���
      * 
      * @return ����¡ ���� ���ڿ�
      * 
      * String paging = contentsProc.pagingBox("list.do", cateno, search_count,
      * nowPage, word); mav.addObject("paging", paging);
      * 
      * mav.addObject("nowPage", nowPage);
      * 
      * // /contents/list_by_cateno_search_paging.jsp
      * mav.setViewName("/contents/list_by_cateno_table_img1_search_paging");
      * 
      * return mav; }
      */
  
  /**
   * ��� + �˻� + ����¡ ����
   * http://localhost:9090/resort/contents/list.do
   * http://localhost:9090/resort/contents/list.do?cateno=1&word=������&nowPage=1
   * @param cateno
   * @param word
   * @param nowPage
   * @return
   */
  @RequestMapping(value = "/contents/list.do", 
                                       method = RequestMethod.GET)
  public ModelAndView list_by_cateno_search_paging_join(
      @RequestParam(value="cateno", defaultValue="1") int cateno,
      @RequestParam(value="word", defaultValue="") String word,
      @RequestParam(value="nowPage", defaultValue="1") int nowPage
      ) { 
    System.out.println("--> nowPage: " + nowPage);
    
    ModelAndView mav = new ModelAndView();
    
    // ���ڿ� ���ڿ� Ÿ���� �����ؾ������� Obejct ���
    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("cateno", cateno); // #{cateno}
    map.put("word", word);     // #{word}
    map.put("nowPage", nowPage);  // �������� ����� ���ڵ��� ������ �����ϱ����� ���     
    
    // �˻� ���
    List<Contents_MemberVO> list = contentsProc.list_by_cateno_search_paging_join(map);
    mav.addObject("list", list);
    
    // �˻��� ���ڵ� ����
    int search_count = contentsProc.search_count(map);
    mav.addObject("search_count", search_count);
  
    CateVO cateVO = cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);
    
    CategrpVO categrpVO = categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    /*
     * SPAN�±׸� �̿��� �ڽ� ���� ����, 1 ���������� ���� 
     * ���� ������: 11 / 22   [����] 11 12 13 14 15 16 17 18 19 20 [����] 
     * 
     * @param listFile ��� ���ϸ� 
     * @param cateno ī�װ���ȣ 
     * @param search_count �˻�(��ü) ���ڵ�� 
     * @param nowPage     ���� ������
     * @param word �˻���
     * @return ����¡ ���� ���ڿ�
     */ 
    String paging = contentsProc.pagingBox("list.do", cateno, search_count, nowPage, word);
    mav.addObject("paging", paging);
  
    mav.addObject("nowPage", nowPage);

    // /contents/list_by_cateno_search_paging.jsp
    mav.setViewName("/contents/list_by_cateno_table_img1_search_paging_join");   
    
    return mav;
  }

  /**
   * ī�װ� �� ��� http://localhost:9090/resort/contents/list_grid1.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/list_by_cateno_grid1.do", method = RequestMethod.GET)
  public ModelAndView list_by_cateno_grid1(int cateno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/list_by_cateno_grid1"); // /webapp/contents/list_by_cateno.jsp

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    List<ContentsVO> list = this.contentsProc.list_by_cateno(cateno);
    mav.addObject("list", list);

    return mav;
  }

  /**
   * ��ȸ http://localhost:9090/resort/contents/read.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/read.do", method = RequestMethod.GET)
  public ModelAndView read(int contentsno) {

    ModelAndView mav = new ModelAndView();
    // mav.setViewName("/contents/read"); // /webapp/contents/read.jsp
    // mav.setViewName("/contents/read_img"); // /webapp/contents/read.jsp
    mav.setViewName("/contents/read_img_attachfile_reply_add_pg"); // /webapp/contents/read.jsp

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    // ÷�� ���� ���
    List<AttachfileVO> attachfile_list = this.attachfileProc.list_by_contentsno(contentsno);
    mav.addObject("attachfile_list", attachfile_list);

    return mav;
  }

//http://localhost:9090/resort/contents/update.do
  /**
   * ���� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/update.do", method = RequestMethod.GET)
  public ModelAndView update(int contentsno) {
    ModelAndView mav = new ModelAndView();

    ContentsVO contentsVO = this.contentsProc.read_update(contentsno); // ������ �б�
    mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

    mav.setViewName("/contents/update"); // webapp/contents/update.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/update.do
  /**
   * ���� ó��
   * 
   * @param contentsVO
   * @return
   */
  @RequestMapping(value = "/contents/update.do", method = RequestMethod.POST)
  public ModelAndView update(ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    // mav.addObject("cateVO", cateVO); // ���޾ȵ�.
    mav.addObject("cate_name", cateVO.getName());
    mav.addObject("cateno", cateVO.getCateno());

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    // mav.addObject("categrpVO", categrpVO); // ���޾ȵ�.
    mav.addObject("categrp_name", categrpVO.getName());

    mav.addObject("contentsno", contentsVO.getContentsno());

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());

    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0; // ������ ���ڵ� ����

    passwd_cnt = this.contentsProc.passwd_check(hashMap);

    if (passwd_cnt == 1) { // �н����� ��ġ
      cnt = this.contentsProc.update(contentsVO);
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����

    System.out.println("cnt : " + cnt + " / passwd_cnt : " + passwd_cnt);

    if (passwd_cnt == 1 && cnt == 1) {
      // mav.setViewName("redirect:/contents/update_msg.jsp"); //
      // webapp/contents/update_msg.jsp
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
      // mav.setViewName("redirect:/contents/list.do"); // spring ��ȣ��
    } else {
      mav.setViewName("/contents/update_msg"); // webapp/contents/update_msg.jsp
    }

    return mav;
  }

  // http://localhost:9090/resort/contents/delete.do
  /**
   * ���� ��
   * 
   * @return
   */
  /*
   * @RequestMapping(value="/contents/delete.do", method=RequestMethod.GET )
   * public ModelAndView delete(int contentsno) { ModelAndView mav = new
   * ModelAndView();
   * 
   * ContentsVO contentsVO = this.contentsProc.read(contentsno); // ������ �б�
   * mav.addObject("contentsVO", contentsVO); //
   * request.setAttribute("contentsVO", contentsVO);
   * 
   * mav.setViewName("/contents/delete"); // webapp/contents/update.jsp
   * 
   * return mav; }
   */

  // http://localhost:9090/resort/contents/delete.do
  /**
   * �α��� ���θ� üũ������ �ٸ� ������� ���� ���� �� �� �ִ� ȭ���� ��µ� ���� ��
   * 
   * @return
   */
  /*
   * @RequestMapping(value="/contents/delete.do", method=RequestMethod.GET )
   * public ModelAndView delete(HttpSession session, int contentsno) {
   * ModelAndView mav = new ModelAndView();
   * 
   * if (this.memberProc.isMember(session)) { ContentsVO contentsVO =
   * this.contentsProc.read(contentsno); // ������ �б� mav.addObject("contentsVO",
   * contentsVO); // request.setAttribute("contentsVO", contentsVO);
   * 
   * mav.setViewName("/contents/delete"); // webapp/contents/update.jsp } else {
   * mav.setViewName("redirect:/member/login_need.jsp"); } return mav; }
   */

//http://localhost:9090/resort/contents/delete.do
  /**
   * �α��� ���θ� üũ������ �ٸ� ������� ���� ���� �� �� �ִ� ȭ���� ��µ� ���� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/delete.do", method = RequestMethod.GET)
  public ModelAndView delete(HttpSession session, int contentsno) {
    ModelAndView mav = new ModelAndView();

    int memberno = (int) session.getAttribute("memberno");

    if (memberno == this.contentsProc.read(contentsno).getMemberno()) {
      ContentsVO contentsVO = this.contentsProc.read(contentsno); // ������ �б�
      mav.addObject("contentsVO", contentsVO); // request.setAttribute("contentsVO", contentsVO);

      mav.setViewName("/contents/delete"); // webapp/contents/update.jsp
    } else {
      mav.setViewName("redirect:/member/mconfirm_fail_msg.jsp");
    }
    return mav;
  }

  // http://localhost:9090/resort/contents/delete.do
  /**
   * ���� ó��
   * 
   * @param contentsVO
   * @return
   */
  @RequestMapping(value = "/contents/delete.do", method = RequestMethod.POST)
  public ModelAndView delete(ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    int contentsno = contentsVO.getContentsno();
    contentsVO = this.contentsProc.read(contentsno);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cate_name", cateVO.getName());
    mav.addObject("cateno", cateVO.getCateno());

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrp_name", categrpVO.getName());

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", contentsVO.getPasswd());

    mav.addObject("title", contentsVO.getTitle());

    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0; // ������ ���ڵ� ����

    passwd_cnt = this.contentsProc.passwd_check(hashMap);

    if (passwd_cnt == 1) { // �н����� ��ġ
      cnt = this.contentsProc.delete(contentsno);
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����

    if (passwd_cnt == 1 && cnt == 1) {
      cateProc.decreaseCnt(contentsVO.getCateno());
    }

    mav.addObject("url", "delete_msg");
    mav.setViewName("redirect:/contents/msg.do");

    return mav;
  }

  /**
   * �����̹��� ����� http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_create.do", method = RequestMethod.GET)
  public ModelAndView img_create(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/img_create");

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    return mav;
  }

  /**
   * �����̹��� ��� ó�� http://localhost:9090/resort/contents/create.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_create.do", method = RequestMethod.POST)
  public ModelAndView img_create(HttpServletRequest request, ContentsVO contentsVO) {
    // System.out.println("IP: " + request.getRemoteAddr()); //0:0:0:0:0:0:0:1 ��
    // �ڱ��ڽ��� ��Ÿ��
    contentsVO.setIp(request.getRemoteAddr());

    ModelAndView mav = new ModelAndView();

    mav.setViewName("redirect:/contents/read.do?contentsno=" + contentsVO.getContentsno());

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());

    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0; // ������ ���ڵ� ����

    passwd_cnt = this.contentsProc.passwd_check(hashMap);

    if (passwd_cnt == 1) { // �н����� ��ġ

      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      String file1 = ""; // main image
      String thumb1 = ""; // preview image

      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      // ���� ������ ����� fnamesMF ��ü�� ������.
      // <input type='file' class="form-control" name='file1MF' id='file1MF'
      // value='' placeholder="���� ����" multiple="multiple">
      MultipartFile mf = contentsVO.getFile1MF();
      long size1 = mf.getSize(); // ���� ũ��
      if (size1 > 0) { // ���� ũ�� üũ
        // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
        // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
        file1 = Upload.saveFileSpring(mf, upDir);

        if (Tool.isImage(file1)) { // �̹������� �˻�
          // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 200, height: 150
          thumb1 = Tool.preview(upDir, file1, 200, 150);
        }

      }

      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------

      cnt = this.contentsProc.img_create(contentsVO);
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����

    return mav;
  }

  /**
   * �����̹��� ������ http://localhost:9090/resort/contents/img_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_update.do", method = RequestMethod.GET)
  public ModelAndView img_update(int contentsno) {
    ModelAndView mav = new ModelAndView();
    mav.setViewName("/contents/img_update");

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    CateVO cateVO = this.cateProc.read(contentsVO.getCateno());
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    return mav;
  }

  /**
   * �����̹��� ���� ó�� ���� �̹��� ������ ���ο� �̹��� ���(���� ó��)
   * http://localhost:9090/resort/contents/img_update.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_update.do", method = RequestMethod.POST)
  public ModelAndView img_update(HttpServletRequest request, ContentsVO contentsVO,
      @RequestParam(value = "nowPage", defaultValue = "1") int nowPage) {
    ModelAndView mav = new ModelAndView();
    int contentsno = contentsVO.getContentsno();
    int cateno = contentsVO.getCateno();
    String passwd = contentsVO.getPasswd();
    System.out.println(contentsno + " " + cateno + " " + passwd);
    mav.setViewName("redirect:/contents/read.do?contentsno=" + contentsno);

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0; // ������ ���ڵ� ����

    passwd_cnt = this.contentsProc.passwd_check(hashMap);

    if (passwd_cnt == 1) { // �н����� ��ġ

      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      // ������ ���� ������ �о��.
      ContentsVO vo = this.contentsProc.read(contentsno);

      String file1 = vo.getFile1().trim(); // main image
      String thumb1 = vo.getThumb1().trim(); // preview image
      long size1 = 0;
      boolean sw = false;

      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      sw = Tool.deleteFile(upDir, vo.getFile1()); // Folder���� 1���� ���� ����
      sw = Tool.deleteFile(upDir, vo.getThumb1()); // Folder���� 1���� ���� ����
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      System.out.println("���� ���� �Ϸ�");
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      System.out.println("upDir ����");
      MultipartFile mf = contentsVO.getFile1MF();
      System.out.println("mf ����" + mf.getSize());
      size1 = mf.getSize(); // ���� ũ��
      if (size1 > 0) { // ���� ũ�� üũ
        // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
        // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
        file1 = Upload.saveFileSpring(mf, upDir);
        if (Tool.isImage(file1)) { // �̹������� �˻�
          // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 200, height: 150
          thumb1 = Tool.preview(upDir, file1, 200, 150);
        }
      }

      vo.setFile1(file1);
      vo.setThumb1(thumb1);
      vo.setSize1(size1);
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      cnt = this.contentsProc.img_update(vo);
    }
    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����
    mav.addObject("nowPage", nowPage); // request�� ����
    return mav;
  }

  /**
   * �����̹��� ���� ó�� http://localhost:9090/resort/contents/img_delete.do
   * 
   * @return
   */
  @RequestMapping(value = "/contents/img_delete.do", method = RequestMethod.POST)
  public ModelAndView img_delete(HttpServletRequest request, int contentsno, int cateno, String passwd,
      @RequestParam(value = "nowPage", defaultValue = "1") int nowPage) {
    ModelAndView mav = new ModelAndView();

    mav.setViewName("redirect:/contents/read.do?contentsno=" + contentsno);

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    int passwd_cnt = 0; // �н����� ��ġ ���ڵ� ����
    int cnt = 0; // ������ ���ڵ� ����

    passwd_cnt = this.contentsProc.passwd_check(hashMap);

    if (passwd_cnt == 1) { // �н����� ��ġ

      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------
      // ������ ���� ������ �о��.
      ContentsVO contentsVO = this.contentsProc.read(contentsno);

      String file1 = contentsVO.getFile1().trim(); // main image
      String thumb1 = contentsVO.getThumb1().trim(); // preview image
      long size1 = contentsVO.getSize1();
      String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
      boolean sw = false;

      if (file1.length() > 0) {
        sw = Tool.deleteFile(upDir, contentsVO.getFile1()); // Folder���� 1���� ���� ����
      }
      if (thumb1.length() > 0) {
        sw = Tool.deleteFile(upDir, contentsVO.getThumb1()); // Folder���� 1���� ���� ����
      }
      file1 = "";
      thumb1 = "";
      size1 = 0;

      contentsVO.setFile1(file1);
      contentsVO.setThumb1(thumb1);
      contentsVO.setSize1(size1);
      // -------------------------------------------------------------------
      // ���� ���� �ڵ� ����
      // -------------------------------------------------------------------

      cnt = this.contentsProc.img_delete(contentsVO);
    }

    mav.addObject("cnt", cnt); // request�� ����
    mav.addObject("nowPage", nowPage); // request�� ����
    mav.addObject("passwd_cnt", passwd_cnt); // request�� ����

    return mav;
  }

//http://localhost:9090/resort/contents/map_create.do?cateno=25&contentsno=28
  /**
   * ���� ��� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/map_create.do", method = RequestMethod.GET)
  public ModelAndView map_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/map_create"); // webapp/contents/map_create.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/passwd.do?contentsno=28&passwd=123
  /**
   * ���� �н����� üũ, JSON ���
   * 
   * @return
   */
  @ResponseBody
  @RequestMapping(value = "/contents/passwd.do", method = RequestMethod.GET, produces = "text/plain;charset=UTF-8")
  public String passwd(int contentsno, String passwd) {
    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    int cnt = this.contentsProc.passwd_check(hashMap);

    JSONObject json = new JSONObject();
    json.put("cnt", cnt);

    return json.toString();
  }

  // http://localhost:9090/resort/contents/map_create.do?cateno=25&contentsno=28&passwd=1234
  /**
   * ���� ���
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param map        ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/map_create.do", method = RequestMethod.POST)
  public ModelAndView map_create(int cateno, int contentsno, String map, String passwd) {
    ModelAndView mav = new ModelAndView();

    // System.out.println("map: " + map);
    // System.out.println("contentsno: " + contentsno);

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("map", map);
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    this.contentsProc.map(hashMap); // ���� ���

    mav.addObject("contentsno", contentsno);

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/map_delete.do?cateno=25&contentsno=28
  /**
   * ���� ���� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/map_delete.do", method = RequestMethod.GET)
  public ModelAndView map_delete(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/map_delete"); // webapp/contents/map_delete.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/map_delete.do?cateno=25&contentsno=28&passwd=1234
  /**
   * ���� ���� ó��
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param map        ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/map_delete.do", method = RequestMethod.POST)
  public ModelAndView map_delete_proc(int cateno, int contentsno, String passwd) {
    ModelAndView mav = new ModelAndView();

    // System.out.println("map: " + map);
    // System.out.println("contentsno: " + contentsno);

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("map", ""); // map ����
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    this.contentsProc.map(hashMap); // ���� ���� ó��

    mav.addObject("contentsno", contentsno);

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/youtube_create.do?cateno=25&contentsno=28
  /**
   * Youtube ��� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/youtube_create.do", method = RequestMethod.GET)
  public ModelAndView youtube_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/youtube_create"); // webapp/contents/youtube_create.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/youtube_create.do?cateno=25&contentsno=28
  /**
   * Youtube ���
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param youtube    ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/youtube_create.do", method = RequestMethod.POST)
  public ModelAndView youtube_create(int cateno, int contentsno, String youtube, String passwd) {
    ModelAndView mav = new ModelAndView();

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("youtube", youtube);
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    this.contentsProc.youtube(hashMap); // ���� ���

    mav.addObject("contentsno", contentsno);

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/youtube_delete.do?cateno=25&contentsno=28
  /**
   * ���� ���� ó��
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param paswd      �н�����
   * @param map        ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/youtube_delete.do", method = RequestMethod.POST)
  public ModelAndView youtube_delete_proc(int cateno, int contentsno, String passwd) {
    ModelAndView mav = new ModelAndView();

    // System.out.println("map: " + map);
    // System.out.println("contentsno: " + contentsno);

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("youtube", ""); // map ����
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    this.contentsProc.youtube(hashMap); // ���� ���� ó��

    mav.addObject("contentsno", contentsno);

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp

    return mav;
  }

//http://localhost:9090/resort/contents/mp3_create.do
  /**
   * MP3 ��� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/mp3_create.do", method = RequestMethod.GET)
  public ModelAndView mp3_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp3_create"); // webapp/contents/mp3_create.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/mp3_create.do
  /**
   * MP3 ���
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param youtube    ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/mp3_create.do", method = RequestMethod.POST)
  public ModelAndView mp3_create(HttpServletRequest request, ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------
    String mp3 = ""; // mp3 ����

    String upDir = Tool.getRealPath(request, "/contents/storage/mp3"); // ���� ���
    // ���� ������ ����� fnamesMF ��ü�� ������.
    MultipartFile mf = contentsVO.getMp3MF(); // ���� ���
    long fsize = mf.getSize(); // ���� ũ��
    if (fsize > 0) { // ���� ũ�� üũ
      // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
      // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
      mp3 = Upload.saveFileSpring(mf, upDir);
    }
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp3", mp3);
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());

    this.contentsProc.mp3(hashMap);

    mav.addObject("contentsno", contentsVO.getContentsno());

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/read.jsp

    return mav;
  }

// http://localhost:9090/resort/contents/mp3_delete.do?cateno=25&contentsno=28
  /**
   * MP3 ���� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/mp3_delete.do", method = RequestMethod.GET)
  public ModelAndView mp3_delete(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp3_delete"); // webapp/contents/mp3_delete.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/mp3_delete.do?cateno=25&contentsno=28
  /**
   * MP3 ���� ó��
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param paswd      �н�����
   * @param map        ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/mp3_delete.do", method = RequestMethod.POST)
  public ModelAndView mp3_delete_proc(HttpServletRequest request, int cateno, int contentsno, String passwd) {
    ModelAndView mav = new ModelAndView();

    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------
    // ������ ���� ������ �о��.
    ContentsVO contentsVO = contentsProc.read(contentsno);
    System.out.println("mp3: " + contentsVO.getMp3());

    String upDir = Tool.getRealPath(request, "/contents/storage/mp3"); // ���� ���
    boolean sw = Tool.deleteFile(upDir, contentsVO.getMp3()); // Folder���� 1���� ���� ����
    System.out.println("sw: " + sw);
    // -------------------------------------------------------------------
    // ���� ���� ���� ����
    // -------------------------------------------------------------------

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp3", ""); // map ����
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    this.contentsProc.mp3(hashMap);

    mav.addObject("contentsno", contentsno);

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp

    return mav;
  }

//http://localhost:9090/resort/contents/mp4_create.do
  /**
   * MP4 ��� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/mp4_create.do", method = RequestMethod.GET)
  public ModelAndView mp4_create(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp4_create"); // webapp/contents/mp4_create.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/mp4_create.do
  /**
   * MP4 ���
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param youtube    ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/mp4_create.do", method = RequestMethod.POST)
  public ModelAndView mp4_create(HttpServletRequest request, ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------
    String mp4 = ""; // mp3 ����

    String upDir = Tool.getRealPath(request, "/contents/storage/mp4"); // ���� ���
    // ���� ������ ����� fnamesMF ��ü�� ������.
    MultipartFile mf = contentsVO.getMp4MF(); // ���� ���
    long fsize = mf.getSize(); // ���� ũ��
    if (fsize > 0) { // ���� ũ�� üũ
      // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
      // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
      mp4 = Upload.saveFileSpring(mf, upDir);
    }
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp4", mp4);
    hashMap.put("contentsno", contentsVO.getContentsno());
    hashMap.put("passwd", contentsVO.getPasswd());

    this.contentsProc.mp4(hashMap);

    mav.addObject("contentsno", contentsVO.getContentsno());

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/read.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/mp4_delete.do
  /**
   * MP4 ���� ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/mp4_delete.do", method = RequestMethod.GET)
  public ModelAndView mp4_delete(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    ContentsVO contentsVO = this.contentsProc.read(contentsno);
    mav.addObject("contentsVO", contentsVO);

    mav.setViewName("/contents/mp4_delete"); // webapp/contents/mp4_delete.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/mp4_delete.do
  /**
   * MP4 ���� ó��
   * 
   * @param cateno     ī�װ� ��ȣ
   * @param contentsno �۹�ȣ
   * @param paswd      �н�����
   * @param map        ���� ��ũ��Ʈ
   * @return
   */
  @RequestMapping(value = "/contents/mp4_delete.do", method = RequestMethod.POST)
  public ModelAndView mp4_delete_proc(HttpServletRequest request, int cateno, int contentsno, String passwd) {
    ModelAndView mav = new ModelAndView();

    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------
    // ������ ���� ������ �о��.
    ContentsVO contentsVO = contentsProc.read(contentsno);
    // System.out.println("mp4: " + contentsVO.getMp4());

    String upDir = Tool.getRealPath(request, "/contents/storage/mp4"); // ���� ���
    boolean sw = Tool.deleteFile(upDir, contentsVO.getMp4()); // Folder���� 1���� ���� ����
    // System.out.println("sw: " + sw);
    // -------------------------------------------------------------------
    // ���� ���� ���� ����
    // -------------------------------------------------------------------

    HashMap<String, Object> hashMap = new HashMap<String, Object>();
    hashMap.put("mp4", ""); // map ����
    hashMap.put("contentsno", contentsno);
    hashMap.put("passwd", passwd);

    this.contentsProc.mp4(hashMap);

    mav.addObject("contentsno", contentsno);

    mav.setViewName("redirect:/contents/read.do"); // webapp/contents/list.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/reply.do?cateno=1&contentsno=1
  /**
   * �亯 ��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/reply.do", method = RequestMethod.GET)
  public ModelAndView reply(int cateno, int contentsno) {
    ModelAndView mav = new ModelAndView();
    System.out.println("�亯 ���: " + contentsno);

    CateVO cateVO = this.cateProc.read(cateno);
    mav.addObject("cateVO", cateVO);

    CategrpVO categrpVO = this.categrpProc.read(cateVO.getCategrpno());
    mav.addObject("categrpVO", categrpVO);

    mav.setViewName("/contents/reply"); // webapp/contents/reply.jsp

    return mav;
  }

  // http://localhost:9090/resort/contents/reply.do
  /**
   * �亯 ��� ó��
   * 
   * @return
   */
  @RequestMapping(value = "/contents/reply.do", method = RequestMethod.POST)
  public ModelAndView reply(HttpServletRequest request, ContentsVO contentsVO) {
    ModelAndView mav = new ModelAndView();
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------
    String file1 = ""; // main image
    String thumb1 = ""; // preview image

    String upDir = Tool.getRealPath(request, "/contents/storage/main_images"); // ���� ���
    // ���� ������ ����� fnamesMF ��ü�� ������.
    MultipartFile mf = contentsVO.getFile1MF();
    long size1 = mf.getSize(); // ���� ũ��
    if (size1 > 0) { // ���� ũ�� üũ
      // mp3 = mf.getOriginalFilename(); // ���� ���ϸ�, spring.jpg
      // ���� ���� �� ���ε�� ���ϸ��� ���ϵ�, spring.jsp, spring_1.jpg...
      file1 = Upload.saveFileSpring(mf, upDir);

      if (Tool.isImage(file1)) { // �̹������� �˻�
        // thumb �̹��� ������ ���ϸ� ���ϵ�, width: 120, height: 80
        thumb1 = Tool.preview(upDir, file1, 250, 200);
      }

    }
    // -------------------------------------------------------------------
    // ���� ���� �ڵ� ����
    // -------------------------------------------------------------------

    contentsVO.setIp(request.getRemoteAddr()); // ������ IP

    // PK return ��
    contentsVO.setFile1(file1);
    contentsVO.setThumb1(thumb1);
    contentsVO.setSize1(size1);

    // --------------------------- �亯 ���� �ڵ� ���� --------------------------
    ContentsVO parentVO = contentsProc.read(contentsVO.getContentsno()); // �θ�� ���� ����

    HashMap<String, Object> map = new HashMap<String, Object>();
    map.put("grpno", parentVO.getGrpno());
    map.put("ansnum", parentVO.getAnsnum());
    contentsProc.increaseAnsnum(map); // ���� ��ϵ� �亯 �ڷ� +1 ó����.

    contentsVO.setGrpno(parentVO.getGrpno()); // �θ��� �׷��ȣ �Ҵ�
    contentsVO.setIndent(parentVO.getIndent() + 1); // �亯 ���� ����
    contentsVO.setAnsnum(parentVO.getAnsnum() + 1); // �θ� �ٷ� �Ʒ� ���
    // --------------------------- �亯 ���� �ڵ� ���� --------------------------

    int cnt = this.contentsProc.reply(contentsVO); // Call By Reference�� ����

    System.out.println("cnt: " + cnt);
    mav.addObject("cnt", cnt);

    // ---------------------------------------------------------------------------------------
    // cnt, contentsno return
    // ---------------------------------------------------------------------------------------
    // Spring <-----> contentsVO <-----> MyBATIS
    // Spring�� MyBATIS�� ContentsVO�� �����ϰ� ����.
    // MyBATIS�� insert�� PK �÷��� contentsno������ ���� ������ PK�� �Ҵ���.
    int contentsno = contentsVO.getContentsno(); // MyBATIS ���ϵ� PK
    System.out.println("contentsno: " + contentsno);
    mav.addObject("contentsno", contentsno); // request�� ����
    // ---------------------------------------------------------------------------------------

    mav.addObject("cateno", contentsVO.getCateno());
    mav.addObject("url", "reply_msg"); // // webapp/contents/reply_msg.jsp

    if (cnt == 1) { // ���������� ���� ��ϵ� ��츸 �� �� ����
      this.cateProc.increaseCnt(contentsVO.getCateno()); // �ۼ� ����
    }
    /*
     * // mav.setViewName("/contents/create_msg"); //
     * mav.setViewName("redirect:/contents/list.do"); // spring ��ȣ��
     * mav.setViewName("redirect:/contents/msg.do"); } else { //
     * mav.setViewName("/contents/create_msg"); // webapp/contents/create_msg.jsp }
     */

    mav.setViewName("redirect:/contents/msg.do");

    return mav;
  }

}
