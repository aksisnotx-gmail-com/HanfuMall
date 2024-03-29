export const JumpDetail = (orderId) => {
    uni.navigateTo({
        url: `/pagesA/pages/my/orderDetail?orderId=${orderId}`
    })
}

