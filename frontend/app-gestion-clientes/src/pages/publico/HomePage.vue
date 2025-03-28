<template>
    <div
        class="home-page container mx-auto relative p-5"
    >
        <!-- Contenido de la página -->
    <!--hro-section-->
    <section class="hero-section py-8">
        <h1 class="font-bold text-3xl ">Soluciones Legales y Contables para Empresas</h1>
        <p class="text-lg font-semibold text-white">Ayudamos a tu empresa a superar los retos legales y financieros con un enfoque personalizado.</p>
        <a href="#contact" class="cta-button hover:underline text-md text-white">Solicita tu consulta gratuita</a>
    </section>
    
    <hr>

    <section class="benefits my-32 py-4">
        <h2 class="text-3xl font-semibold text-sky-600 mb-4">¿Por qué elegirnos?</h2>
        <p class="text-lg my-2">En Buenrostro y Asociados, ofrecemos servicios de alta calidad para garantizar el éxito de tu empresa. Estos son solo algunos de los beneficios que tendrás al elegirnos:</p>
        <ul class="my-8 space-y-4">
            <li><strong>Experiencia en Litigios:</strong> Más de 10 años de experiencia en la resolución de disputas comerciales.</li>
            <li><strong>Consultoría Contable Especializada:</strong> Asesoría financiera adaptada a las necesidades de tu empresa.</li>
            <li><strong>Compromiso con la Excelencia:</strong> Nos esforzamos por ofrecer soluciones claras y efectivas.</li>
        </ul>
    </section>
    <section class="testimonials my-32 py-4">
        <h2 class="font-semibold text-3xl text-sky-600 mb-4">Lo que dicen nuestros clientes</h2>
        <blockquote>
            <p>"Buenrostro y Asociados nos ayudaron a resolver una disputa importante con éxito. Son altamente profesionales y muy confiables." - <strong>Juan Pérez, CEO de ABC Corp.</strong></p>
        </blockquote>
        <blockquote>
            <p>"Excelente servicio contable. Nos han ayudado a optimizar nuestra estructura fiscal y aumentar nuestras ganancias." - <strong>Ana López, Directora Financiera de XYZ Ltd.</strong></p>
        </blockquote>
    </section>



    </div>
</template> 

<script lang="ts" setup></script>

<style scoped>
.home-page{
    position: relative;
}
.hero-section {
    position: relative;
    height: 387px;
    margin-top: 2rem;
    background-image: url('../../assets/imgs/quintillisass.png');
    background-position: center;
    background-size: cover;
    background-repeat: no-repeat;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    border-radius: 8px;
    text-align: center;

}

</style>
