<template>
    <div class=" container mx-auto">

        <section class="services-intro">
            <h1>Nuestros Servicios</h1>
            <p>Ofrecemos soluciones personalizadas para cada uno de tus desafíos legales y contables.</p>
        </section>

        <section class="my-32 min-h-80 mx-auto p-5 mt-6 w-fit gap-5 sm:gap-10 md:gap-28 flex items-center justify-between ">
            <article class="w-8/12 flex flex-col items-center h-fit w-fit space-y-9">
                <h1 class="self-start font-medium text-2xl">Titlle Section</h1>
                <div class=" self-start space-y-1">
                    <p>Lorem ir sit. Loonsectetur adipisicing elit. Nostrum, veritatis?</p>
                    <p>Lorem, ipsum dolor sit amt consectetur adipisicing elit.</p>
                    <p class="">itationem laboriosam non laborum architecto saepe adipiscidawdawd .</p>
                    <p>hola ecomo esaes ,...</p>
                </div>

                <a href="#" class="btn-offset self-start">
                    More Info
                </a>
                
            </article>
            <article class="articulo">
                <div class="image"></div>
                
            </article>

        </section>

        <section class="services-details">
            <h2>Servicios Legales</h2>
            <h3>Litigio Comercial y Resolución de Disputas</h3>
            <p>Ofrecemos servicios de litigio especializados para empresas, incluyendo disputas contractuales, propiedad intelectual y más.</p>
            
            <h2>Servicios Contables</h2>
            <h3>Consultoría y Auditoría Financiera</h3>
            <p>Brindamos asesoría contable para mejorar la estructura financiera de tu empresa, incluyendo auditorías fiscales, análisis financieros y planificación tributaria.</p>
        </section>


        <div class="my-20 flex gap-8 items-center justify-center">
            <span class="h-[2px] w-24 bg-red-600"></span>
            <div class="text-lg text-center space-y-0 ">
                <h2 class="font-semibold">Lorem, ipsum.</h2>
                <h2 class="font-semibold">some</h2>
            </div>
            <span class="h-[2px] w-28 bg-red-600"></span>
        </div>
        <section class="w-full my-28 px-2 flex flex-wrap items-center justify-evenly border">
            <article class="card">
                <div class="img"></div>
                <div class = "card-content">
                    <h1 class="text-2xl font-semibold">Title</h1>
                    <p>Something content this section...</p>
                    <button class="btn-offset">see more</button>
                </div>
            </article>
            <article class="card">
                <div class="img"></div>
                <div class = "card-content">
                    <h1 class="text-2xl font-semibold">Title</h1>
                    <p>Something content this section...</p>
                    <button class="btn-offset">see more</button>
                </div>
            </article>
            <article class="card">
                <div class="img"></div>
                <div class = "card-content">
                    <h1 class="text-2xl font-semibold">Title</h1>
                    <p>Something content this section...</p>
                    <button class="btn-offset">see more</button>
                </div>
            </article>
            <article class="card">
                <div class="img"></div>
                <div class = "card-content">
                    <h1 class="text-2xl font-semibold">Title</h1>
                    <p>Something content this section...</p>
                    <button class="btn-offset">see more</button>
                </div>
            </article>
        </section>

        <section class="success-cases">
            <h2>Casos de Éxito</h2>
            <p>Estos son algunos de los proyectos exitosos que hemos llevado a cabo para nuestros clientes:</p>
            
            <div class="case">
                <h3>Resolución de Disputa Comercial</h3>
                <p>Asesoramos a XYZ Ltd. en un litigio que resultó en una resolución favorable, ahorrando millones en posibles pérdidas.</p>
            </div>
            
            <div class="case">
                <h3>Consultoría Contable para Ahorro Fiscal</h3>
                <p>Ayudamos a ABC Corp. a optimizar su estructura tributaria, lo que resultó en un ahorro del 20% en impuestos.</p>
            </div>
        </section>


    </div>
</template>

<script lang="ts" setup>

</script>

<style scope>
.articulo {
    position: relative;
    height: 335px;
    width: 725px;
    overflow: hidden;
    border-radius: 10px;
}
.image {
    position: absolute;
    height: 100%;
    width: 100%;
    background-image: url('../../assets/imgs/img-1.jpeg');
    background-repeat: no-repeat;
    background-size: cover;
    border-radius: 10px;
    scale: 0.95;
    transition:all .7s ease;
}

.articulo:hover .image{

    transform: rotate(-15deg);
    scale: 1.7;
}

.card{
    position: relative;
    padding: 0;
    height: 345px;
    width: 255px;
    overflow: hidden;
    border-radius: 10px;
    margin: 1.5rem;
    
}
.card-content{
    position: absolute;
    width: 100%;
    height: 100%;
    left: 0;
    bottom: 100%;
    background: rgba(0, 0, 0, 0.4);
    display: flex;
    flex-direction: column;
    justify-content: space-evenly;
    align-items: center;
    color: #fff;
    transition: top 7.s ease;

}

.card:hover .card-content{
    top: 0%;
}
.card .img {
    position: absolute;
    height: 100%;
    width: 100%;
    background-image: url('../../assets/imgs/img-1.jpeg');
    background-repeat: no-repeat;
    background-size: cover;
    border-radius: 10px;
}
</style>