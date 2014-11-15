
package org.cxt.lt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final boolean DEBUG = true;

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // 模板文件夹
        String templateFolderPath;
        // 材料文件夹
        String materialFolderPath;
        // 成品文件夹
        String productFolderPath;

        if (DEBUG || (3 == args.length)) {

            if (DEBUG) {
                templateFolderPath = "C:\\Users\\Administrator\\Desktop\\PicRename\\template";
                materialFolderPath = "C:\\Users\\Administrator\\Desktop\\PicRename\\material";
                productFolderPath = "C:\\Users\\Administrator\\Desktop\\PicRename\\product";
            } else {
                templateFolderPath = args[0];
                materialFolderPath = args[1];
                productFolderPath = args[2];
            }

            List<File> templateFiles = readFolderFiles(new File(templateFolderPath));
            List<File> materialFiles = readFolderFiles(new File(materialFolderPath));
            File productFolder = new File(productFolderPath);
            if (productFolder.exists()) {
                if (0 < productFolder.list().length) {
                    System.out.println("Please empty product first. path : " + productFolderPath);
                    return;
                }
            } else {
                boolean ret = productFolder.mkdirs();
                if (!ret) {
                    System.out.println("Create product folder fail. path : " + productFolderPath);
                    return;
                }
            }

            // 开始加工材料
            for (File material : materialFiles) {
                boolean find = false;
                for (File template : templateFiles) {
                    boolean result = Util.compareImageBinary(material, template);
                    System.out.println("compare " + material.getAbsolutePath() + " , "
                            + template.getAbsolutePath() + " " + result);
                    if (result) {
                        // 找到与模板相同的图，输出成品
                        Util.copy(material, new File(productFolder, template.getName()));
                        find = true;
                        break;
                    }
                }
                if (!find) {
                    //没找到，原样输出
                    Util.copy(material, new File(productFolder, material.getName()));
                }
            }

            System.out.println("Mission complete!!!");
        }
    }

    private static List<File> readFolderFiles(File aFile) {

        List<File> ret = new ArrayList<File>();

        for (File child : aFile.listFiles()) {
            if (child.isFile()) {
                ret.add(child);
            } else if (child.isDirectory()) {
                List<File> grandsons = readFolderFiles(child);
                for (File grandson : grandsons) {
                    ret.add(grandson);
                }
            }
        }

        return ret;
    }

}
