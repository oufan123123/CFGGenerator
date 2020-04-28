package Util.Tool;

import java.io.File;

public class DirDelete {
			private String dirPath="";
			
			public DirDelete(){
			}
			public boolean deleteDir(File dir) {
		        if (dir.isDirectory()) {
		            String[] children = dir.list();
		            for (int i=0; i<children.length; i++) {
		                boolean success = deleteDir(new File(dir, children[i]));
		                if (!success) {
		                    return false;
		                }
		            }
		        }
		        // 目录此时为空，可以删除
		        return dir.delete();
		    }
}
