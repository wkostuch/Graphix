Graph file format for 2D undicted graphs:

A-x1,y1 : B-x2,y2 :: w1
A2-x2,y2 : B2-x2,y2 
. . .
An-xn,yn : Bn-x(n+1),y(n+1) :: wn

Ax-xk,yk 

xi,yi = coordinates of ith vertex
A/An, b/Bn are the names for the Vertex
w1/wn = weight of that edge
If there are two vertices on a line, there's an edge between them.
If there's no weight given, then it defaults to the distance between the Vertices
The "xk,yk" example is for a vertex with no edges.