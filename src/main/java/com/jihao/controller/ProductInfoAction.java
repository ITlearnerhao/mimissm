package com.jihao.controller;

import com.github.pagehelper.PageInfo;
import com.jihao.pojo.ProductInfo;
import com.jihao.pojo.vo.ProductInfoVo;
import com.jihao.service.ProductInfoService;
import com.jihao.utils.FileNameUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sound.sampled.Line;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/prod")
public class ProductInfoAction {
    //每页显示的条数
    public static final int PAGE_SIZE = 5;
    String saveFileName = "";
    @Autowired
    ProductInfoService productInfoService;

    //商品不分页
    @RequestMapping("/getAll")
    public String getAll(HttpServletRequest request) {
        List<ProductInfo> list = productInfoService.getAll();
        request.setAttribute("list", list);
        return "product";
    }

    //显示第一页的五条数据
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo pageInfo = null;
        Object vo = request.getSession().getAttribute("prodVo");
        if (vo != null){
            pageInfo = productInfoService.splitPageVo((ProductInfoVo) vo, PAGE_SIZE);
            request.getSession().removeAttribute("prodVo");
        } else {
            //得到第一页的数据
            pageInfo = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.setAttribute("info", pageInfo);
        return "product";
    }

    //Ajax分页处理
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(ProductInfoVo vo, HttpSession session) {
        //取得当前page参数的页面的数据
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);
    }

    //多条件功能查询
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductInfoVo vo,HttpSession session){
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list);
    }

    //ajax异步文件上传
    @RequestMapping("/ajaxImg")
    @ResponseBody
    public Object ajaxImg(MultipartFile pimage, HttpServletRequest request) {
        //提取生成文件名UUID+上传图片的后缀名
        saveFileName = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //得到项目中图片存储的路径
        String str = request.getServletContext().getRealPath("/image_big");
        //转存      File.separator 文件提供的\
        try {
            pimage.transferTo(new File(str + File.separator + saveFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //返回json对象，封装图片的路径，实现在页面的回显
        JSONObject object = new JSONObject();
        object.put("imgurl", saveFileName);
        //必须要进行json的转化
        return object.toString();
    }

    @RequestMapping("/save")
    public String save(ProductInfo info,HttpServletRequest request){
        info.setpImage(saveFileName);
        info.setpDate(new Date());
        int num = -1;
        try {
            num = productInfoService.save(info);  //ctrl +alt +t
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num>0){
            request.setAttribute("msg", "增加成功！");
        }else {
            request.setAttribute("msg", "增加失败！");
        }
        //清空saveFileName变量中的内容，为了下次的修改或者增加操作的ajax的上传处理
        saveFileName="";
        //增加成功后，应该重新访问数据库，所以要跳转到分页显示的action上
        return "forward:/prod/split.action";
    }

    @RequestMapping("/one")
    public String one(int pid, ProductInfoVo vo,Model model,HttpSession session){
        ProductInfo info = productInfoService.getByID(pid);
        model.addAttribute("prod", info);
        //将多条件及页码放入到session中，更新处理结束后分页时读取条件和页码进行处理
        session.setAttribute("prodVo", vo);
        return "update";
    }

    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request){
        //对saveFileName变量进行判断，有数据则说明上传过图片；
        //没有数据，使用隐藏作用表单提交数据
        if (!saveFileName.equals("")){
            info.setpImage(saveFileName);
        }
        //完成更新处理
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg", "更新成功!!!");
        }else {
            request.setAttribute("msg", "更新失败!!!");
        }
        //处理完数据之后，要对saveFileName进行清空，防止下次数据传入失败
        saveFileName="";
        return "forward:/prod/split.action";//使用redirect进行操作，不会弹出信息
    }

    @RequestMapping("/delete")
    public String  delete(int pid,ProductInfoVo vo,HttpServletRequest request){
        int num = -1;
        try {
            num = productInfoService.delete(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (num > 0){
            request.setAttribute("msg", "删除成功!!!");
            request.getSession().setAttribute("deleteProdVo", vo);
        }else {
            request.setAttribute("msg", "删除失败!!!");
        }
        //删除以后要跳转到分页显示，但是之前的方法没有返回值
        return "forward:/prod/deleteAjaxSplit.action";
    }

    @ResponseBody
    @RequestMapping( value = "/deleteAjaxSplit",produces = "text/html;charset=UTF-8")
    public Object deleteAjaxSplit(HttpServletRequest request){
        PageInfo info = null;
        Object vo = request.getSession().getAttribute("deleteProdVo");
        if (vo != null){
            info = productInfoService.splitPageVo((ProductInfoVo) vo,PAGE_SIZE);
            request.getSession().removeAttribute("deleteProdVo");
        }else {
            //取得第一页的数据
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        //使用session是因为request读不到load的数据，读不到请求作用域大的数据
        return request.getAttribute("msg");
    }

    //批量删除商品
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids,HttpServletRequest request){
        //将上传上来的字符串隔开，形成商品id的字符数组
        String []ps = pids.split(",");
        try {
            int num = productInfoService.deleteBatch(ps);
            if (num > 0){
                request.setAttribute("msg", "批量删除成功!!!");
            }else {
                request.setAttribute("msg", "批量删除失败!!!");
            }
        } catch (Exception e) {
             request.setAttribute("msg", "商品不可删除！！");
        }
        return "forward:/prod/deleteAjaxSplit.action";
    }

}
