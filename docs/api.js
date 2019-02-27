'use strict';

window.onload = () => {
    const endPoints = [
        {
            method: 'post',
            description: 'Login',
            url: 'URL',
            params: [
                { name: 'username', type: 'string', },
                { name: 'username', type: 'string', },
            ],
            response_examples: [
                { description: 'not authorized', code: 401, body: '', }
            ]
        },
        {
            method: 'post',
            description: 'Login',
            url: 'URL',
            params: [
                { name: 'username', type: 'string', },
                { name: 'username', type: 'string', },
            ],
            response_examples: [
                { description: 'not authorized', code: 401, body: '', }
            ]
        },
        {
            method: 'post',
            description: 'Login',
            url: 'URL',
            params: [
                { name: 'username', type: 'string', },
                { name: 'username', type: 'string', },
            ],
            response_examples: [
                { description: 'not authorized', code: 401, body: '', }
            ]
        },
    ];
    console.log(endPoints)
    const rendered = endPoints.map((endPoint) => `
            <tr class="endpoint-heading">
				<td class="${endPoint.method}"></td>		
                <td>${endPoint.url}</td>
                <td>${endPoint.description}</td>
                <td><button class="details-btn">V</button></td>
            </tr>
            <tr class="endpoint-details" style="display:none;">
				<td colspan="4">
					<h4>parameters:</h4>
					<table class="parameters">
						<thead>
							<tr>
								<td>name</td>
								<td>type</td>
							</tr>
						</thead>
						<tbody>
                            ${endPoint.params.map((param) => `
                                <tr>
                                    <td>${param.name}</td>
                                    <td>${param.type}</td>
                                </tr>
                            `)}
						</tbody>
					</table>
			
			
					<h4>response examples:</h4>
                    <table>
                        ${endPoint.response_examples.map((response) => `
                            <tr>
                                <td>${response.code}</td>
                                <td>OK</td>
                                <td>${response.description}</td>
                            </tr>
                            <tr>
                                <td colspan="3"><code>${response.code}</code></td>
                            </tr>
                        `)}
					</table>
				</td>
			</tr>
			
    `);
    console.log(rendered)

    rendered.forEach((endpoint) => document.querySelector('tbody.endpoints').insertAdjacentHTML('beforeend', endpoint));

    const detailsButtons = document.getElementsByClassName("details-btn");
    Array.from(detailsButtons).forEach(function (element) {
        element.addEventListener('click', (event) => {
            const nextSibling = event.target.parentElement.parentElement.nextElementSibling;
            nextSibling.style.display = nextSibling.style.display === 'none' ? 'block' : 'none'; 
        });
    });
};