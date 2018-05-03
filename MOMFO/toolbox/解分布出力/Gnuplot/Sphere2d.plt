set view equal
#set view 60,135 ##3�����`�ʎ��ɂ����錩��p�x
#set view 66, 100
set size square
#set format "" ##���ڐ����쐬������
set ticslevel 0
unset tics
unset key
set border 63 lw 2
set parametric

ns = 3

#�僁�����̕�
s = 3

set ur[0:1]
set vr[0:1]

u(u) = int(u)
v(v) = int(v)


##�O���t�̑傫��
ur = s*5

set isosample 10
set xr[0:ur]
set yr[0:ur]


set xtics s
set ytics s

##���������̐�(���ݒ�: ��ڐ��Ԃ�1��)
set mxtics 2
set mytics 2
#set tics scale s*0.5

##�ڐ��Ǝ��Ƃ̋���
set xtics offset 0.0,graph 0.05
set ytics offset 0.95,graph 0.00



##Pareto Front�̐ݒ�
#plot PFData pt 7 lc rgb "black" 
##����ȊO�̏o��
#plot DataFile lc rgb "red" lw 1

set term emf enhanced 
set terminal emf font 'Times new Roman' 30
set output OutputFile
plot PFData pt 7 lc rgb "black" 
plot DataFile lc rgb "red" lw 1 pt 7
#replot
unset output
set term windows