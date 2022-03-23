
function addRow(){
	var parentTable = document.getElementById('myTable');
	var productsForm = document.getElementById('add_products_form');
	var myTd1, myTd2, myTd3, myTd4, myInput1, myInput2, myInput3_1, myInput3_2, myInput4;
	var myTr = document.createElement('tr');
	myTr.setAttribute('class', 'unit-table');
	
	myTd1 = document.createElement('td');
	myTd1.innerHTML = 'Enter Product Name:';
	myInput1 = document.createElement('input');
	myInput1.setAttribute('type', 'text');
	myInput1.setAttribute('th:field', '${prd.name}');
	myInput1.setAttribute('placeholder', 'Enter Here');
	myTd1.appendChild(myInput1);
	
	myTd2 = document.createElement('td');
	myTd2.innerHTML = 'Enter Price:';
	myInput2 = document.createElement('input');
	myInput2.setAttribute('type', 'number');
	myInput2.setAttribute('th:field', '${prd.price}');
	myInput2.setAttribute('placeholder', '0');
	myTd2.appendChild(myInput2);
	
	myTd3 = document.createElement('td');
	myTd3.innerHTML = 'In Stock:';
	myInput3_1 = document.createElement('input');
	myInput3_2 = document.createElement('input');
	myInput3_1.innerHTML = 'Yes';
	myInput3_2.innerHTML = 'No';
	myInput3_1.setAttribute('type', 'radio');
	myInput3_1.setAttribute('value', '1');
	myInput3_2.setAttribute('value', '0');
	myInput3_2.setAttribute('type', 'radio');
	myInput3_1.setAttribute('th:field', '${prd.inStock}');
	myInput3_2.setAttribute('th:field', '${prd.inStock}');
	myTd3.appendChild(myInput3_1);
	myTd3.appendChild(myInput3_2);
	
	myTd4 = document.createElement('td');
	myTd4.innerHTML = 'Enter Image:';
	myInput4 = document.createElement('input');
	myInput4.setAttribute('type', 'file');
	myInput4.setAttribute('th:field', '${prd.file}');
	myTd4.appendChild(myInput4);
	
	myTr.appendChild(myTd1);
	myTr.appendChild(myTd2);
	myTr.appendChild(myTd3);
	myTr.appendChild(myTd4);
	
	parentTable.appendChild(myTr); 
	productsForm.append(parentTable);
}
document.getElementById('my_button').onclick = addRow;
