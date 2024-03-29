<script setup>
    import WaitPay from './components/WaitPay.vue'
    import WaitReceive from './components/WaitReceive.vue'
    import WaitEvaluate from './components/WaitEvaluate.vue'
    import all from './components/all.vue'

    const current = ref(3)

    const orderStatus = ref([
        { name: '待付款' },
        { name: '待收货' },
        { name: '待评价' },
        { name: '全部' }
    ])

    const onChangeTab = (args) => {
        const { index, name } = args
        current.value = index
    }

    onLoad((options) => {
        const { index } = options
        current.value = Number(index)
    })
</script>

<template>
    <view class="min-h-100vh bg-#f4f4f4 pb-3">
        <view class="h-13">
            <view class="bg-#fff fixed top-0 left-0 right-0">
                <u-tabs
                    :scrollable="false"
                    lineColor="#7794CF"
                    lineWidth="45"
                    :activeStyle="{ 'color': '#7794CF', 'fontWeight': 'bold' }"
                    :list="orderStatus"
                    @change="onChangeTab"
                    :current="current"
                ></u-tabs>
            </view>
        </view>

        <template v-if="current == 0">
            <WaitPay></WaitPay>
        </template>
        <template v-else-if="current == 1">
            <WaitReceive></WaitReceive>
        </template>
        <template v-else-if="current == 2">
            <WaitEvaluate></WaitEvaluate>
        </template>
        <template v-else>
           <all></all>
        </template>
    </view>
</template>