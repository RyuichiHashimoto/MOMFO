set view equal
set view 60,135
#set view 66, 100
set size square
set ticslevel 0
set format ""
unset key
unset tics
set border 63 lw 2
set parametric

ns = 10

set ur[0:1]
set vr[0:1]

u(u) = int(u)
v(v) = int(v)

p = pi/2
s = 1.0


f1(u,v) = cos(p*u)*cos(p*v)*s
f2(u,v) = cos(p*u)*sin(p*v)*s
f3(u,v) = sin(p*u)*s


z1(u,v)=u*v*0.5
z2(u,v)=u*(1.0-v)*0.5
z3(u,v)=(1.0-u)*0.5

a = 2

g1(u,v) = f1(u,v)**a
g2(u,v) = f2(u,v)**a
g3(u,v) = f3(u,v)**a

#GECCO2017
#ur = s*1.1
#other
ur = s*1.2

set isosample 10
#set xr[-4:0]
#set yr[-4:0]
#set zr[-4:0]
set xr[0:ur]
set yr[0:ur]
set zr[0:ur]
set xtics s
set ytics s
set ztics s
set tics scale 2

#set view 80,65

#splot f1(u,v),f2(u,v),f3(u,v),z1(u,v),z2(u,v),z3(u,v) lc rgb "red" lw 4
#splot f1(u,v),f2(u,v),f3(u,v) lc rgb "red" lw 4

#set datafile separator "	"
#splot s-g1(u,v),s-g2(u,v),s-g3(u,v)

set term emf enhanced
set output OutputFile
splot f1(u,v),f2(u,v),f3(u,v) lc rgb "red" lw 4
replot DataFile pt 7 lc rgb "black" ps 1

#replot
unset output

set term wxt