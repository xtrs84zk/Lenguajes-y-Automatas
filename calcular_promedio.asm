	ld	cero
	st sum
	st cant
loop	get
jz	done
add	sum
st sum
ld	one
add cant
j loop
done ld sum
div cant	prom
ld	prom
out
halt
zero const 0
one const 1
sum const c
prom const d
cant const f