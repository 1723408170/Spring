package com.javapandeng.controller;

import com.javapandeng.base.BaseController;
import com.javapandeng.po.Number1;
import com.javapandeng.po.TjNum;
import com.javapandeng.service.NumberService;
import com.javapandeng.utils.Pager;
import com.javapandeng.utils.SystemContext;
import com.javapandeng.utils.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/number")
public class Numbercontroller extends BaseController {

    @Autowired
    private NumberService numberService;

    /**
     * 分页查询商品列表
     */
    @RequestMapping("/findBySql")
    public String findBySql(Model model, Number1 number1){
        String sql = "select * from number where isDelete = 0 ";
        if(!isEmpty(number1.getName())){
            sql += " and name like '%" + number1.getName() + "%' ";
        }
        sql += " order by id desc";
        Pager<Number1> pagers = numberService.findBySqlRerturnEntity(sql);
        model.addAttribute("pagers",pagers);
        model.addAttribute("obj", number1);
        return "number/Mnum";
    }


    /**
     * 添加商品入口
     */
    @RequestMapping("/add")
    public String add(Model model){
        String sql = "select * from number where isDelete = 0 ";
        List<Number1> listBySqlReturnEntity = numberService.listBySqlReturnEntity(sql);
        model.addAttribute("types",listBySqlReturnEntity);
        return "number/add";
    }

    /**
     * 执行添加商品
     */
    @RequestMapping("/exAdd")
    public String exAdd(Number1 number1, @RequestParam("file") CommonsMultipartFile[] files, HttpServletRequest request) throws IOException {
        numberCommon(number1, files, request);
        number1.setIsDelete(0);

        numberService.insert(number1);
        return "redirect:/number/findBySql.action";
    }

    /**
     * 修改商品入口
     */
    @RequestMapping("/update")
    public String update(Integer id,Model model){
        Number1 obj = numberService.load(id);
        String sql = "select * from number where isDelete = 0 ";
        List<Number1> listBySqlReturnEntity = numberService.listBySqlReturnEntity(sql);
        model.addAttribute("types",listBySqlReturnEntity);
        model.addAttribute("obj",obj);
        return "number/update";
    }

    /**
     * 执行修改商品
     */
    @RequestMapping("/exUpdate")
    public String exUpdate(Number1 number1, @RequestParam("file")CommonsMultipartFile[] files, HttpServletRequest request) throws IOException {
        numberCommon(number1, files, request);
        numberService.updateById(number1);
        return "redirect:/number/findBySql.action";
    }

    private void numberCommon(Number1 number1, @RequestParam("file") CommonsMultipartFile[] files, HttpServletRequest request) throws IOException {
        if(files.length>0) {
            for (int s = 0; s < files.length; s++) {
                String n = UUIDUtils.create();
                String path = SystemContext.getRealPath() + "\\resource\\ueditor\\upload\\" + n + files[s].getOriginalFilename();
                File newFile = new File(path);
                //通过CommonsMultipartFile的方法直接写文件
                files[s].transferTo(newFile);
                if (s == 0) {
                    number1.setUrl1(request.getContextPath()+"\\resource\\ueditor\\upload\\" + n + files[s].getOriginalFilename());
                }

            }
        }


    }

    /**
     * 商品下架
     */
    @RequestMapping("/delete")
    public String update(Integer id){
        Number1 obj = numberService.load(id);
        obj.setIsDelete(1);
        numberService.updateById(obj);
        return "redirect:/number/findBySql.action";
    }

    @RequestMapping("/shoplist")
    public String shoplist(Number1 number1, String condition, Model model){
        String sql = "select * from number where isDelete=0";
        if(!isEmpty(number1.getId())){
            sql +=" and id = " +number1.getId();
        }

        Pager<Number1> pagers = numberService.findBySqlRerturnEntity(sql);
        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",number1);
        return "number/shoplist";
    }

    @RequestMapping("/view")
    public String view(Integer id,Model model){
        Number1 obj = numberService.load(id);
        model.addAttribute("obj",obj);
        return "number/view";
    }

    @RequestMapping("/view1")
    public String view1(Integer id,Model model){
        Number1 obj = numberService.load(id);
        model.addAttribute("obj",obj);
        return "number/view1";
    }
    @RequestMapping("/view2")
    public String view2(Integer id,Model model){
        Number1 obj = numberService.load(id);
        model.addAttribute("obj",obj);
        return "number/view2";
    }




    /*
    客户页面显示
*
**/
    @RequestMapping("/num")
    public String num(Model model, Number1 number1, HttpServletRequest request){
        //折扣商品
        List<Number1> zks = numberService.listBySqlReturnEntity("select * from number where isDelete=0 ");
        model.addAttribute("zks",zks);

        String sql = "select * from number where isDelete=0";
        Pager<Number1> pagers = numberService.findBySqlRerturnEntity(sql);
        model.addAttribute("pagers",pagers);
        model.addAttribute("obj",number1);
        return "/number/num";
    }

    @RequestMapping(value = "/tj")
    public String tj(Number1 number1, Model model, HttpServletRequest request, HttpServletResponse response) {
        //分页查询
        String sql = "SELECT * FROM number WHERE isDelete = 0 ";
        sql += " ORDER BY ID DESC ";
        List<Number1> list = numberService.listBySqlReturnEntity(sql);
        List<Map<String,Object>> maps = new ArrayList<Map<String,Object>>();
        List<TjNum> res = new ArrayList<TjNum>();
        if (!CollectionUtils.isEmpty(list)){
            for (Number1 c : list){
                TjNum td = new TjNum();
                int tot = 0;
                List<Number1> listBySqlReturnEntity = numberService.listBySqlReturnEntity("SELECT * FROM number WHERE 1=1 and isDelete =0 "+c.getId());
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("id",c.getId());
                map.put("name", c.getName());
                map.put("address",c.getAddress());
                map.put("nownum", c.getNownum());
                map.put("num1",c.getNum1());
                map.put("num2", c.getNum2());
                map.put("num3",c.getNum3());
                map.put("num4", c.getNum4());
                map.put("num5",c.getNum5());
                maps.add(map);
            }
        }
        //存储查询条件
        model.addAttribute("maps", maps);
        return "number/view";
    }

}
