set view equal
#set view 60,135 ##3次元描写時における見る角度
#set view 66, 100
set size square
#set format "" ##軸目盛を作成するやつ
set ticslevel 0
unset tics
unset key
set border 63 lw 2
set parametric

ns = 3

#主メモリの幅
s = 3

set ur[0:1]
set vr[0:1]

u(u) = int(u)
v(v) = int(v)


##グラフの大きさ
ur = s*5

set isosample 10
set xr[0:ur]
set yr[0:ur]


set xtics s
set ytics s

##複メモリの数(現設定: 主目盛間に1つ)
set mxtics 2
set mytics 2
#set tics scale s*0.5

##目盛と軸との距離
set xtics offset 0.0,graph 0.05
set ytics offset 0.95,graph 0.00



##Pareto Frontの設定
#plot PFData pt 7 lc rgb "black" 
##それ以外の出力
#plot DataFile lc rgb "red" lw 1

set term emf enhanced 
set terminal emf font 'Times new Roman' 30
set output OutputFile
plot PFData pt 7 lc rgb "black" 
plot DataFile lc rgb "red" lw 1 pt 7
#replot
unset output
set term windows