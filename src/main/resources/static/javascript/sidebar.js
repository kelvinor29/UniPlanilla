const sidebar = document.querySelector(".sidebar");
const sidebarToggler = document.querySelector(".sidebar-toggler");
const menuToggler = document.querySelector(".menu-toggler");
const navLinks = document.querySelectorAll(".nav-link");

let alturaSidebarColapsado = "56px"; // Altura del 'navbar' en dispositivos mmobile

// Quitar o agregar clase al clickear el botón correspondiente del sidebar
sidebarToggler.addEventListener("click", () => {
  sidebar.classList.toggle("collapsed");
});

// Quitar o agregar clase al clickear en el burger del sidebar
menuToggler.addEventListener("click", () => {
  cambiarMenuMovil(sidebar.classList.toggle("menu-active"));
});

// Actualizar la altura del sidebar en mobile
const cambiarMenuMovil = (esMenuAbierto) => {
  sidebar.style.height = esMenuAbierto
    ? `${sidebar.scrollHeight}px`
    : alturaSidebarColapsado;
  menuToggler.querySelector("i").classList = esMenuAbierto
    ? "fa-solid fa-bars-staggered"
    : "fa-solid fa-bars";
};

// Evento para actualizar clase de los links
navLinks.forEach(link => {
  link.addEventListener("click", function(){
    //Quitar clase
    navLinks.forEach(l => l.classList.remove("active"));

    // Agregar clase 
    this.classList.add("active");
  })
});
