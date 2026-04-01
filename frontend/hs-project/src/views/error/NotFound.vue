<script setup>
	import { useRouter } from 'vue-router';
	const router = useRouter();

	const goToMain = () => {
  		router.push('/'); 
	};
</script>
<template>
	<div class="section bg-light">
		<div class="container">

			<div class="row justify-content-center" data-aos="fade-up" data-aos-delay="200">
				<div class="col-md-5">
					<div class="bg-white box-shadow mt-5 mb-5 p-5">
						<h3 class="text-center pt-3">페이지를 찾을 수 없습니다.</h3>
						<hr class="mt-4">
						
						<div class="my-5">
							<div class="text-center">
								<p class="text-center">
									죄송합니다.<br> <strong>404 - 요청하신 페이지가 존재하지 않습니다.</strong>
								</p>
							</div>
						</div>
	                    
						<div>
							<button type="button" class="btn-accent btn-lg w-100" @click="goToMain">
								메인화면으로 이동 <i class="bi bi-arrow-counterclockwise"></i>
							</button>
						</div>
					</div>
				</div>
			</div>

		</div>
	</div>
</template>
