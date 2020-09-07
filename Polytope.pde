
int scale = 200;

int dim0 = 0;
int dim1 = 1;

Hypercomplex[] verts;
int[][] edges;

Hypercomplex rotation;

void setup() {
  size(1000, 800);
  background(20, 10, 15);
  
  verts = Helpers.parseVerts(loadStrings("verts.csv"));
  edges = Helpers.parseEdges(loadStrings("edges.csv"));
  
  rotation = Hypercomplex.of(100, 1, .25, -.25, .2, .2, -.1, .1);
  rotation = rotation.div(rotation.norm());
}


void draw() {
  clear();
  
  pushMatrix();
  translate(width / 2F, height / 2F);
  
  stroke(40);
  for(int i = 0; i < edges.length; i++) {
    if(i % 10 == 0) {
      float[] parts0 = verts[edges[i][0]].flatten();
      float[] parts1 = verts[edges[i][1]].flatten();
      line(parts0[dim0] * scale, parts0[dim1] * scale, parts1[dim0] * scale, parts1[dim1] * scale);
    }
  }
  
  stroke(255);
  for(int i = 0; i < verts.length; i++) {
    float[] parts = verts[i].flatten();
    ellipse(parts[dim0] * scale, parts[dim1] * scale, 3, 3);
  }
  
  popMatrix();
  
  text("Viewing dimensions [" + dim0 + ", " + dim1 + "]", 10, 20);
  
  for(int i = 0; i < verts.length; i++) {
    verts[i] = verts[i].mul(rotation);
  }
}

void mousePressed(MouseEvent event) {
  if(event.getButton() == LEFT) {
    do {
      dim0 += 1;
      dim0 %= 8;
    }
    while(dim0 == dim1);
  }
  if(event.getButton() == RIGHT) {
    do {
      dim1 += 1;
      dim1 %= 8;
    }
    while(dim0 == dim1);
  }
}
