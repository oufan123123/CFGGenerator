package Util.Tool;

public class QuickSort {
    public QuickSort(){
    	
    }
    public void quick(double []n){
    	if(n.length>0){
    		quickSort(n, 0, n.length-1);
    	}
    }
    public void quickSort(double []n ,int left,int right){  
        int pivot;  
        if (left < right) {  
            //pivot作为枢轴，较之小的元素在左，较之大的元素在右  
            pivot = partition(n, left, right);  
            //对左右数组递归调用快速排序，直到顺序完全正确  
            quickSort(n, left, pivot - 1);  
            quickSort(n, pivot + 1, right);  
        }  
    }
    public int partition(double[]n ,int left,int right){  
        double pivotkey = n[left];  
        //枢轴选定后永远不变，最终在中间，前小后大  
        while (left < right) {  
            while (left < right && n[right] >= pivotkey) --right;  
            //将比枢轴小的元素移到低端，此时right位相当于空，等待低位比pivotkey大的数补上  
            n[left] = n[right];  
            while (left < right && n[left] <= pivotkey) ++left;  
            //将比枢轴大的元素移到高端，此时left位相当于空，等待高位比pivotkey小的数补上  
            n[right] = n[left];  
        }  
        //当left == right，完成一趟快速排序，此时left位相当于空，等待pivotkey补上  
        n[left] = pivotkey;  
        return left;  
    }  
}
