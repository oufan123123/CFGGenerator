package APKData.SmaliGraph;

public class Color {
			private String R="";
			private String G="";
			private String B="";
			
			public Color(){}
			public Color(String r, String g, String b){
				this.R=r;
				this.G=g;
				this.B=b;
			}

			public String getR() {
				return R;
			}

			public void setR(String r) {
				R = r;
			}

			public String getG() {
				return G;
			}

			public void setG(String g) {
				G = g;
			}

			public String getB() {
				return B;
			}

			public void setB(String b) {
				B = b;
			}
			public String getRGB(){
				String resultString="";
				resultString +="R: "+this.R+", G: "+this.G+", B: "+this.B;
				return resultString;
			}
}
