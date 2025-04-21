document.addEventListener("DOMContentLoaded", () => {

  const sidebar = document.querySelector(".sidebar");
  const sidebarToggler = document.querySelector(".sidebar-toggler");
  const menuToggler = document.querySelector(".menu-toggler");
  const navLinks = document.querySelectorAll(".nav-link");

  let alturaSidebarColapsado = "56px"; // Altura del 'navbar' en dispositivos mobile

  // Quitar o agregar clase al clickear el botón correspondiente del sidebar
  sidebarToggler.addEventListener("click", () => {
    sidebar.classList.toggle("collapsed");
  });

  // Quitar o agregar clase al clickear en el burger del sidebar
  menuToggler.addEventListener("click", () => {
    cambiarMenuMovil(sidebar.classList.toggle("menu-active"));
  });

  // Toggle para móviles
  menuToggler.addEventListener("click", () => {
    const esSidebarAbierto = sidebar.classList.toggle("menu-active");
    sidebar.style.height = esSidebarAbierto ? `${sidebar.scrollHeight}px` : alturaSidebarColapsado;

    const icono = menuToggler.querySelector("i");
    if (icono)
      icono.className = esSidebarAbierto ? "fa-solid fa-bars-staggered" : "fa-solid fa-bars";
  });

  // TODO: Validar el link seleccionado del sidebar
  navLinks.forEach(link => {
    link.addEventListener("click", () => {
      navLinks.forEach(l => l.classList.remove("active"));
      link.classList.add("active");
    });
  });

});
