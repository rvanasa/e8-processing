import java.util.ArrayList;
import java.util.List;

public final class Helpers {

    public static Hypercomplex[] parseVerts(String[] lines) {
        Hypercomplex[] verts = new Hypercomplex[lines.length];

        for(int i = 0; i < lines.length; i++) {

            List<Float> parts = new ArrayList<>();
            for(String s : lines[i].split(",")) {
                parts.add(Float.parseFloat(s));
            }
            float[] array = new float[parts.size()];
            for(int j = 0; j < parts.size(); j++) {
                array[j] = parts.get(j);
            }

            verts[i] = Hypercomplex.of(array);
        }

        return verts;
    }

    public static int[][] parseEdges(String[] lines) {
        int[][] edges = new int[lines.length][];

        for(int i = 0; i < lines.length; i++) {
            String[] split = lines[i].split(",");
            edges[i] = new int[] {Integer.parseInt(split[0]), Integer.parseInt(split[1])};
        }
        return edges;
    }

}
