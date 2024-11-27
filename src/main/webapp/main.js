function svgHandler(event) {
    const svg = document.getElementById("graphSvg");
    const rect = svg.getBoundingClientRect();
    try {
        let r = document.getElementById('mainForm:hiddenR').value;
        if (r) {
            x = ((event.clientX - rect.left - 150) * (r / 2) / (50)).toFixed(2);
            y = (((-1) * (event.clientY - rect.top - 150)) * (r / 2) / (50)).toFixed(2);
            document.getElementById('mainForm:hiddenX').value = x;
            document.getElementById('mainForm:hiddenY').value = y;

            document.getElementById('mainForm:submitButton').click();
        }
    } catch (e){
        alert(e.message);
    }

}
