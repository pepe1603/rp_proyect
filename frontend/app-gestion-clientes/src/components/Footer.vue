<template>
    <footer class="bg-gray-800 min-h-36 relative text-white text-center p-4 pb-1 flex flex-col items-start justify-between">
        <div class="w-full flex items-center justify-between">
            <div class="contact">
                <h1 class="font-2xl font-semibold my-4">Contacto:</h1>
                <div class="contact-icons flex gap-4">
                    <div class="icon1 icon flex flex-col items-center"><span class="p-2 bg-white rounded-full"><PhoneIcon class="w-4 h-4 text-gray-800 "/> </span>  </div>
                    <div class="icon2 icon flex flex-col items-center"><span class="p-2 bg-white rounded-full"><MapPinIcon class="w-4 h-4 text-gray-800 "/> </span>  </div>
                    <div class="icon3 icon flex flex-col items-center"><span class="p-2 text-yellow-600 bg-white rounded-full"><ChatBubbleBottomCenterTextIcon class="w-4 h-4 text-gray-800 "/> </span>  </div>
                </div>

            </div>
            <div class="social">
                <h1 class="font-2xl font-semibold my-4">Síguenos:</h1>
                    <div class="social-icons flex gap-4">
                        <!-- Facebook -->
                        <div class="icon icon-facebook flex flex-col items-center">
                            <span class="p-2 bg-[#0866ff] rounded-full">
                            <FacebookIcon class="w-4 h-4 fill-slate-100 text-white"  />
                            </span>
                        </div>
                        <!-- Twitter -->
                        <div class="icon icon-twitter flex flex-col items-center">
                            <span class="p-2 bg-gray-400 rounded-full">
                            <TwitterIcon class="w-4 h-4 fill-slate-100 text-white" />
                            </span>
                        </div>
                        <!-- Instagram -->
                        <div class="icon icon-instagram flex flex-col items-center">
                            <span class="p-2 bg-[#e4405f] rounded-full">
                            <InstagramIcon class="w-4 h-4 fill-slate-100 text-white" />
                            </span>
                        </div>
                        <!-- LinkedIn -->
                        <div class="icon icon-linkedin flex flex-col items-center">
                            <span class="p-2 bg-[#0a66c2] rounded-full">
                            <LinkedInIcon class="w-4 h-4 fill-slate-100 text-white" />
                            </span>
                        </div>
                        <!-- WhatsApp -->
                        <div class="icon icon-whatsapp flex flex-col items-center">
                            <span class="p-2 bg-[#25d366] rounded-full">
                            <WhatsAppIcon class="w-4 h-4 fill-slate-100 text-white" />
                            </span>
                        </div>
                    </div>
            </div>
            
        </div>
        <smaller class="w-full relative  mx-auto hover:text-slate-300" ><p>&copy; 2024 Todos los derechos reservados</p></smaller>
    </footer>
</template>

<script setup>
import { PhoneIcon, MapPinIcon, ChatBubbleBottomCenterTextIcon } from '@heroicons/vue/24/solid'
//import FacebookIcon  from '@/assets/icons/facebook.svg';

import * as FacebookIcon from '@/assets/icons/facebook.svg';
import TwitterIcon from '@/assets/icons/x.svg';
import InstagramIcon from '@/assets/icons/instagram.svg';
import LinkedInIcon from '@/assets/icons/linkedin.svg';
import WhatsAppIcon from '@/assets/icons/whatsapp.svg';
console.log(FacebookIcon);


</script>

<style scope>
.icon1 span{
    background: #2f6bed;
    scale: 0.95;
    transition: scale .4s ease;
    transition: box-shadow .4s ease;
    
}
.icon2 span{
    background: #10b981;
    scale: 0.95;
    transition: scale .4s ease;
    transition: box-shadow .4s ease;
    
}
.icon3 span{
    background: #ca8a04;
    scale: 0.95;
    transition: scale .4s ease;
    transition: box-shadow .4s ease;
    
}
.icon span > *{
    color: #fff;
    scale: 0.95;
    transition: scale .4s ease;
    transition: box-shadow .4s ease;

}

.icon {
    scale: 0.95;
    transition: scale .4s ease;
    transition: box-shadow .4s ease;
}

.icon:hover{
    scale: 1.05;
}
.icon:hover span{
    box-shadow: 0 0 6px #fff;
}
</style>