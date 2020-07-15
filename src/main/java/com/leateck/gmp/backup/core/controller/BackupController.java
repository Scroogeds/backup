package com.leateck.gmp.backup.core.controller;

import cn.hutool.core.util.RuntimeUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>Title: BackupController</p>
 * <p>Description: </p>
 * <p>@Copyright: Shanghai 2020</p>
 *
 * @author luyangqian
 * version 1.0
 * <pre>History
 *          2020-07-09   luyangqian  Created
 * </pre>
 */
@RestController
@RequestMapping("/backup")
public class BackupController {

    /**
     * 执行命令
     * @param cmd
     * @return
     */
    @GetMapping("/command")
    public String executeCommand(@RequestParam("cmd") String cmd){
        return RuntimeUtil.execForStr(cmd);
    }
}
